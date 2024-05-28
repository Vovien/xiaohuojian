package com.jmbon.minitools.report.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.context
import com.blankj.utilcode.util.ActivityUtils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.Form
import com.jmbon.middleware.extention.getViewModel
import com.jmbon.middleware.utils.BitmapUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.MiniTypeEnum
import com.jmbon.middleware.utils.hasValue
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.jmbon.middleware.utils.toWxMiniProgram
import com.jmbon.minitools.databinding.ActivityPublicTemplateLayoutBinding
import com.jmbon.minitools.databinding.HeaderPublicTemplateLayoutBinding
import com.jmbon.minitools.report.adapter.FertilityGroupChatAdapter
import com.jmbon.minitools.report.bean.FertilityAbilityTestResultBean
import com.jmbon.minitools.report.bean.TemplateTypeEnum
import com.jmbon.minitools.report.fragment.DefaultHeaderFragment
import com.jmbon.minitools.report.fragment.FertilityAbilityTestAuditFragment
import com.jmbon.minitools.report.fragment.FertilityAbilityTestFragment
import com.jmbon.minitools.report.viewmodel.FertilityAbilityTestViewModel
import com.jmbon.minitools.report.viewmodel.GroupChatViewModel
import com.jmbon.minitools.router.MiniToolRouter
import com.qmuiteam.qmui.kotlin.onClick

@Route(path = MiniToolRouter.MINI_TOOL_PUBLIC_TEMPLATE)
class PublicTemplateActivity :
    ViewModelActivity<GroupChatViewModel, ActivityPublicTemplateLayoutBinding>() {


    val vm by lazy {
        context.getViewModel(FertilityAbilityTestViewModel::class.java)
    }

    @JvmField
    @Autowired
    var formList: ArrayList<Form>? = null

    @JvmField
    @Autowired
    var source: String = ""

    /**
     * 模板类型 @see TemplateTypeEnum
     */
    @JvmField
    @Autowired
    var type: Int = 0

    @JvmField
    @Autowired
    var data: Parcelable? = null

    /**
     * 试管自测-首次日程
     */
    @JvmField
    @Autowired
    var scheduleDays = ""

    /**
     * 试管自测-首次花费
     */
    @JvmField
    @Autowired
    var cost = ""

    /**
     * 试管自测-成功率
     */
    @JvmField
    @Autowired
    var successRate = 0f

    private val headerBinding by lazy {
        HeaderPublicTemplateLayoutBinding.inflate(layoutInflater)
    }

    private val groupChatAdapter by lazy {
        FertilityGroupChatAdapter().apply {
            headerBinding.apply {
                addHeaderView(root)
                flBg.onClick {
                    toWxMiniProgram(
                        ActivityUtils.getTopActivity(),
                        type = MiniTypeEnum.MINI_TYPE_FERTILITY_SERVICE.value
                    )
                }
                root.post {
                    val fragment = when (type) {
                        TemplateTypeEnum.TEMPLATE_TYPE_FERTILITY_ABILITY_TEST.value ->
                            if (Constant.isAuditMode)
                                FertilityAbilityTestAuditFragment.newInstance(this@PublicTemplateActivity.data)
                            else FertilityAbilityTestFragment.newInstance(this@PublicTemplateActivity.data)

                        else -> DefaultHeaderFragment()
                    }
                    supportFragmentManager.beginTransaction().apply {
                        add(fcvContainer.id, fragment)
                        commitAllowingStateLoss()
                    }
                }
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .apply {
                if (type == TemplateTypeEnum.TEMPLATE_TYPE_TUBE_TEST.value) {
                    binding.title.setTitleBarColor(Color.WHITE)
                    statusBarColorInt(Color.WHITE)
                } else {
                    transparentStatusBar()
                }
            }
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.TRANSPARENT)
            .init()

        binding.title.apply {
            val statusBarHeight =
                ImmersionBar.getStatusBarHeight(this@PublicTemplateActivity)
            setPadding(0, statusBarHeight, 0, 0)
            leftImageButton.onClick {
                finish()
            }
        }
        binding.llOperate.isVisible = Constant.isAuditMode
        binding.clTubeTest.onClick {
            MiniToolRouter.selfTest(formList, source)
            finish()
        }

        binding.tvCustomScheme.onClick {
            vm?.getTestResultPicture((data as FertilityAbilityTestResultBean).id)
        }

        binding.rvContent.init(groupChatAdapter, dividerHeight = 16f, vertical = false)
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        viewModel.groupChatListLD.observe(this) {
//            it?.icon.hasValue { it1 ->
//                headerBinding.ivChat.load(it1)
//            }
            it?.title.hasValue { it1 ->
                headerBinding.tvGroupChat.text = it1
            }
            it?.description.hasValue { it1 ->
                headerBinding.tvDesc.text = it1
            }
            groupChatAdapter.setList(emptyList())
            if (Constant.isAuditMode) {
                headerBinding.groupAll.isVisible = false
            } else {
                headerBinding.groupAll.isVisible = true
                groupChatAdapter.setList(it?.group_list)
            }
        }
        viewModel.getGroupChatList()

        vm?.testResultPictureLD?.observe(this) {
            savePicture(it)
        }
    }

    /**
     * 下载并保存图片到相册
     */
    private fun savePicture(url: String?) {
        if (url.isNullOrBlank()) {
            return
        }

        GlideUtil.getInstance().loadUrlSimpleTarget(url, object :
            GlideUtil.GlideUtilSimpleTarget {
            override fun onResourceReady(resource: Drawable?) {
                val bitmap = resource?.toBitmapOrNull() ?: return
                this@PublicTemplateActivity.let { it1 ->
                    val result = BitmapUtils.saveBitmap(
                        it1,
                        bitmap,
                        "${it1.cacheDir.path}/${System.currentTimeMillis()}.jpeg"
                    )
                    if (result) {
                        "已保存测试表单信息\n可在本地相册查看".showToast()
                    }
                }
            }

            override fun onLoadFailed() {

            }

        })
    }
}
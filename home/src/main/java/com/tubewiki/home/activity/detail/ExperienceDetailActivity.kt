package com.tubewiki.home.activity.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.network.config.HttpCode
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.gyf.immersionbar.ktx.statusBarHeight
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.bean.*
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.Constant.addExperienceDetailCount
import com.jmbon.middleware.js.JsImageLoad
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.utils.AnimatorUtils.startLightAnimation
import com.jmbon.middleware.valid.action.Action
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.widget.html.X5WebView
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tubewiki.home.R
import com.tubewiki.home.bean.ExperienceDetailData
import com.tubewiki.home.databinding.ActivityExperienceDetailContentBinding
import com.tubewiki.home.databinding.IncludeExperienceDetailBinding
import com.tubewiki.home.dialog.WeChatDialog
import com.umeng.commonsdk.internal.crash.UMCrashManager
import kotlinx.coroutines.delay
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs

/**
 * 经验详情页面
 * @author MilkCoder
 * @date 2023/11/23
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = "/home/experience/details")
class ExperienceDetailActivity :
    ViewModelActivity<ExperienceDetailViewModel, ActivityExperienceDetailContentBinding>(),
    NetworkUtils.OnNetworkStatusChangedListener {
    private val x5WebView by lazy {
        X5WebView(this)
    }

    var webScrollY = 0

    // 文章 ID
    @Autowired(name = TagConstant.EXPERIENCE_ID)
    @JvmField
    var experienceId = 0

    @Autowired(name = TagConstant.TOPIC_ID)
    @JvmField
    var topicId = 0

    override fun beforeViewInit() {
        super.beforeViewInit()
        supportActionBar?.hide()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    @SuppressLint("MissingPermission")
    override fun initView(savedInstanceState: Bundle?) {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        // 设置按钮为 loading 状态
        if (NetworkUtils.isConnected()) {
            initPageState()
        }
        initListener()
        initContentView()
    }

    private fun initListener() {
        NetworkUtils.registerNetworkStatusChangedListener(this)
        binding.imageMore.setOnClickListener {
            val reportListData = arrayListOf(
                CustomDialogTypeBean(
                    resources.getString(com.jmbon.widget.R.string.currency_cancle), 3
                ) as MultiItemEntity,
            )
            if (experienceId != 0) {
                reportListData.add(
                    0, CustomDialogTypeBean(
                        "举报该经验贴",
                        2
                    )
                )
            }

            XPopup.Builder(this).isDestroyOnDismiss(true).dismissOnTouchOutside(true)
                .dismissOnBackPressed(true).autoDismiss(true).enableDrag(false)
                .asCustom(CustomListBottomDialog(this, reportListData) {
                    //举报回答
                    when (it) {
                        0 -> Action {
                            ARouter.getInstance().build("/question/activity/report")
                                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                                .withString(TagConstant.REPORT_TYPE, "experience")
                                .withString(TagConstant.TARGET_ID, experienceId.toString())
                                .withString(
                                    TagConstant.TARGET_CONTENT,
                                    viewModel.experienceDetailFlow.value?.experience?.content
                                )
                                .navigation()
                        }.logInToIntercept()
                    }
                }).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initContentView() {
        binding.content.scrollerLayout.addView(x5WebView, 3)
        x5WebView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        x5WebView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        x5WebView.apply {
            settings?.loadWithOverviewMode = true
            settings?.allowContentAccess = true
            addJavascriptInterface(JsImageLoad(this@ExperienceDetailActivity), "imageLoader")
            addJavascriptInterface(this@ExperienceDetailActivity, "android")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(p0: WebView?, p1: String?) {
                    resumed {
                        // 加载完成后等待300毫秒再重置高度，为防止图片未加载完成就重置高度
                        delay(300)
                        x5WebView.loadUrl("javascript:window.android.resize(document.body.clientHeight)")
                    }
                    super.onPageFinished(p0, p1)
                    if (!this@ExperienceDetailActivity.isFinishing) {
                        loadDataWhenFinish()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    url?.let {
//                        H5ArouterUtils.urlProcessing(it)
//                    }
                    return true
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
//                    request?.url.toString().let {
//                        H5ArouterUtils.urlProcessing(it)
//                    }
                    return true
                }
            }
        }

        // 计算文章内容的一页高度
        val screenHeight =
            ScreenUtils.getScreenHeight() - statusBarHeight - 20.dp - binding.content.llOperate.height
        binding.content.scrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->

            var rate =
                abs(scrollY).toFloat() / 92f.dp()
            webScrollY = scrollY - x5WebView.top
            if (scrollY < 100) {
                rate = 0f
            }
            binding.textName.alpha = rate
            if (experienceId != 0 && viewModel.firstShowFlow.value && scrollY >= screenHeight + (screenHeight / 2)) {
                showPopupImageDialog()
            }

            if (experienceId != 0 && Constant.enterExperienceDetailCount == 1 && viewModel.firstShowFlow.value) {
                showPopupImageDialog()
            }

        }
    }

    private fun showPopupImageDialog() {
        val popupImage = CommonViewModel.popupImageFlow.value?.popup_adv
        popupImage?.apply {
            if (this.popupImg.isNotNullEmpty()) {
                val dialog = WeChatDialog(ActivityUtils.getTopActivity(), this)
                dialog.showDialog()
                viewModel.firstShowFlow.value = false
            }
        }
    }

    @JavascriptInterface
    fun resize(height: Float) {
        ThreadUtils.runOnUiThread {
            val layoutParams: ViewGroup.LayoutParams = x5WebView.layoutParams
            layoutParams.height = (height * ScreenUtils.getScreenDensity()).toInt()
            x5WebView.layoutParams = layoutParams
            binding.content.scrollerLayout.checkLayoutChange()
        }
    }

    override fun initData() {

    }

    private var webPageInit = false
    override fun getData() {
        if (experienceId != 0) {
            started {
                viewModel.getExperienceDetail(experienceId, topicId).netCatch {
                    showErrorState()
                    if (code.toInt() == HttpCode.HTTP.NOT_RESOURCES) {
                        showContentState()
                        //文章不存在
                        binding.content.emptyHint.visible()
                    }
                }.next {
                    initExperienceDetails(this)
                }
            }
            viewModel.getPopupImage()
        }
    }

    private fun initExperienceDetails(it: ExperienceDetailData?) {
        binding.content.apply {
            it?.let { item ->
                addExperienceDetailCount()
                clTubeTest.isVisible = item.leftButton != null
                tvCustomScheme.isVisible = item.rightButton != null
                tvTubeTest.setTextWhenNotEmpty(item.leftButton?.title)
                tvCustomScheme.setTextWhenNotEmpty(item.rightButton?.title)
                tvLabel.setTextWhenNotEmpty(item.rightButton?.label)
                clLabel.isVisible = !item.rightButton?.label.isNullOrBlank()
                if (clLabel.isVisible) {
                    tvAnim.startLightAnimation()
                }
                clTubeTest.setOnClickListener {
                    item.leftButton?.let {
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = it.item_type,
                                identity = it.identity
                            )
                        )
                    }
                }
                tvCustomScheme.onClick {
                    item.rightButton?.let {
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = it.item_type,
                                identity = it.identity
                            )
                        )
                    }
                }
                clAd.setData(item.recommendCircle)
                item.experience?.apply {
                    if (!webPageInit) {
                        binding.content.setPageLoad(title, infos)
                    }
                    loadH5(content)
                    user?.apply {
                        tvName.text = userName
                        ivAvatar.loadCircle(avatarFile)
                        tvExperienceTag.visible()
                        tvShareNum.text = subTitle
                    } ?: kotlin.run {
                        clUser.isVisible = false
                    }
                }

            }
        }
        showContentState()
    }

    private fun IncludeExperienceDetailBinding.setPageLoad(
        title: String,
        infoList: MutableList<ExperienceDetailData.Experience.Info>
    ) {
        setTitleWithTime(HtmlTools.delHTMLTag(title))
        // 圈子参考资料
        if (infoList.isNotNullEmpty()) {
            llMainContent.visibility = View.VISIBLE
            createInfoItem(infoList, llInfo)
        } else {
            llMainContent.visibility = View.GONE
        }
    }

    private fun IncludeExperienceDetailBinding.setTitleWithTime(
        title: String
    ) {
        textTitle.text = title
        binding.textName.text = title
    }

    private fun loadH5(
        h5Str: String,
    ) {
        x5WebView.loadDataWithBaseURL(
            BuildConfig.H5_URL,
            ExperienceHtmlTools.createExperienceHtml(h5Str),
            "text/html",
            "utf-8",
            null
        )
        x5WebView.loadDataWhenFinish()
    }


    private fun X5WebView.loadDataWhenFinish() {
        showContentState()
    }

    private fun createInfoItem(
        infoList: MutableList<ExperienceDetailData.Experience.Info>,
        content: LinearLayout
    ) {
        val textParams =
            LinearLayout.LayoutParams(90f.dp(), LinearLayout.LayoutParams.WRAP_CONTENT)
        val textParams2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        textParams2.weight = 1.0f
        infoList.forEach {
            val itemView = LinearLayout(this)
            itemView.orientation = LinearLayout.HORIZONTAL
            val startText = createTextView(it.key ?: "")
            startText.setPadding(0, 16f.dp(), 10f.dp(), 16f.dp())
            startText.layoutParams = textParams
            itemView.addView(startText)
            val endText = createTextView(it.value ?: "")
            endText.setPadding(16f.dp(), 16f.dp(), 0, 16f.dp())
            endText.layoutParams = textParams2
            endText.setTextColor(R.color.color_262626.Color)
            itemView.addView(endText)
            content.addView(itemView)
        }
    }

    private fun createTextView(str: String): TextView {
        val text = TextView(this@ExperienceDetailActivity)
        text.apply {
            isSingleLine = false
            setText(str)
            setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
            textSize = 16f
        }
        return text
    }

    override fun onDisconnected() {
    }

    override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        getData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            getData()
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        try {
            binding.content.scrollerLayout.removeView(x5WebView)
            x5WebView.destroy()
        } catch (e: Exception) {
            UMCrashManager.reportCrash(Utils.getApp(), e)
        } finally {
            super.onDestroy()
        }
        NetworkUtils.unregisterNetworkStatusChangedListener(this)
    }

    override fun finish() {
        overridePendingTransition(
            R.anim.activity_bottom_out,
            R.anim.activity_background
        )
        super.finish()
    }

}
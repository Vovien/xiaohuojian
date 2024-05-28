package com.jmbon.minitools.report.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.bean.Form
import com.jmbon.middleware.config.Constant
import com.jmbon.minitools.databinding.ActivityTestLayoutBinding
import com.jmbon.minitools.report.adapter.SelfTestFormAdapter
import com.jmbon.minitools.report.bean.TemplateTypeEnum
import com.jmbon.minitools.report.viewmodel.FertilityAbilityTestViewModel
import com.jmbon.minitools.router.MiniToolRouter
import com.jmbon.minitools.tubetest.util.ForecastAnimatorHelper
import com.jmbon.minitools.tubetest.util.TubeArouterConstant.MINI_TOOL_SELF_TEST
import kotlinx.coroutines.delay

@Route(path = MINI_TOOL_SELF_TEST)
class SelfTestActivity :
    ViewModelActivity<FertilityAbilityTestViewModel, ActivityTestLayoutBinding>() {

    @JvmField
    @Autowired
    var formList: ArrayList<Form>? = null

    @JvmField
    @Autowired
    var source: String = ""

    private var timeout = false

    /**
     * 选择列表
     */
    private val choiceValueList = mutableListOf<Int>()

    /**
     * 预测中动画
     */
    private val forecastAnimatorHelper by lazy {
        ForecastAnimatorHelper()
    }

    private val formAdapter by lazy {
        SelfTestFormAdapter(source) {
            val uri = Uri.parse(it)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }.apply {
            setList(formList)
            setItemClickListener {
                choiceValueList.add(it.`val`)
                if (binding.vpContent.currentItem < data.size - 1) {
                    binding.vpContent.currentItem = binding.vpContent.currentItem + 1
                } else {
                    startLoadingAnim()
                    viewModel.getTestResult(formList?.getOrNull(0)?.typeId ?: 0, choiceValueList)
                }
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.vpContent.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 1
            adapter = formAdapter
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        if (formList.isNullOrEmpty()) {
            "测试数据为空, 请稍后重试~".showToast()
            finish()
            return
        }

        viewModel.testResultLD.observe(this) {
            if (timeout) {
                toResultPage(true)
            }
        }
    }

    private fun startLoadingAnim() {
        binding.flLoading.isVisible = true
        forecastAnimatorHelper.startForecastAnimator2(
            binding.viewBg,
            binding.slView,
            binding.tvForecast
        )
        launch {
            delay(2000)
            toResultPage()
            timeout = true
        }
    }

    private fun toResultPage(showToast: Boolean = false) {
        viewModel.testResultLD.value?.let {
            if (Constant.isAuditMode) {
                MiniToolRouter.toPublicTemplate(
                    TemplateTypeEnum.TEMPLATE_TYPE_FERTILITY_ABILITY_TEST,
                    it, formList, source
                )
            } else {
                if (it.score >= 85) {
                    MiniToolRouter.toPublicTemplate(
                        TemplateTypeEnum.TEMPLATE_TYPE_FERTILITY_ABILITY_TEST,
                        it
                    )
                } else {
                    MiniToolRouter.toFertilityAbilityTestResult(it)
                }
            }


            finish()
        } ?: kotlin.run {
            if (showToast) {
                binding.flLoading.isVisible = false
                forecastAnimatorHelper.stopAnimator()
                "数据异常, 请稍后重试".showToast()
            }
        }
    }
}
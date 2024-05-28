package com.jmbon.minitools.report.view

import android.os.Bundle
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.helper.setScaleImage
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.ActivityFertilityAbilityTestResultLayoutBinding
import com.jmbon.minitools.report.bean.FertilityAbilityTestResultBean
import com.jmbon.minitools.router.MiniToolRouter
import com.jmbon.minitools.tubetest.adapter.ImageTextBannerAdapter
import com.qmuiteam.qmui.kotlin.onClick

/******************************************************************************
 * Description: 生育力测试结果页
 *
 * Author: jhg
 *
 * Date: 2023/9/16
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = MiniToolRouter.MINI_TOOL_FERTILITY_ABILITY_TEST_RESULT)
class FertilityAbilityTestResultActivity :
    ViewModelActivity<CommonViewModel, ActivityFertilityAbilityTestResultLayoutBinding>() {

    @Autowired
    @JvmField
    var fertilityAbilityTestResultBean: FertilityAbilityTestResultBean? = null

    private val bannerAdapter by lazy {
        ImageTextBannerAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        binding.initFertilityTestResult()

        binding.ivClose.onClick {
            finish()
        }

        binding.tvOperator.onClick {
            fertilityAbilityTestResultBean?.button?.apply {
                BannerHelper.onClick(
                    CommonBanner(
                        item_type = this.itemType,
                        identity = this.identity
                    )
                )
            }
        }
    }

    /**
     * 初始化生育力自测结果页
     */
    private fun ActivityFertilityAbilityTestResultLayoutBinding.initFertilityTestResult() {
        bannerTip.setAdapter(bannerAdapter)
        bannerTip.setUserInputEnabled(false)
        bannerTip.addBannerLifecycleObserver(this@FertilityAbilityTestResultActivity)
        bannerAdapter.setDatas(fertilityAbilityTestResultBean?.user_list)
        bannerTip.isVisible = !fertilityAbilityTestResultBean?.user_list.isNullOrEmpty()
        ivTubeTestResult.setImageResource(R.drawable.common_result_fetility_test_icon)
        tvTubeTestTitle.text = "您的生育力得分偏低"
        tvTubeTestDesc.text = "建议添加医生助手，寻求医生建议"
        tvTubeTestResult.text = fertilityAbilityTestResultBean?.result
        flTubeTestResult.isVisible = !fertilityAbilityTestResultBean?.result.isNullOrBlank()
        ivQuote.isVisible = flTubeTestResult.isVisible
        buildSpannedString {
            append("医生建议 - ")
            bold {
                append("示例")
            }
            tvTubeTestExampleTitle.text = this
        }
        binding.ivTubeTestExample.setScaleImage(fertilityAbilityTestResultBean?.image)
        tvOperator.setTextWhenNotEmpty(fertilityAbilityTestResultBean?.button?.title)
    }
}
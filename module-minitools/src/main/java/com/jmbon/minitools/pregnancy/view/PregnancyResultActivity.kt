package com.jmbon.minitools.pregnancy.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.allen.library.helper.ShapeBuilder
import com.allen.library.helper.ShapeGradientAngle
import com.apkdv.mvvmfast.base.DataBindingActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.databinding.ActivityPregnancyBinding
import com.jmbon.minitools.pregnancy.adapter.PregnancyHelpGroupAdapter
import com.jmbon.minitools.pregnancy.util.PregnancyRouterConstant
import com.jmbon.minitools.pregnancy.viewmodel.PregnancyViewModel
import com.jmbon.minitools.tubetest.util.ForecastAnimatorHelper
import com.jmbon.widget.interpolator.EaseCubicInterpolator
import com.qmuiteam.qmui.kotlin.onClick


@Route(path = PregnancyRouterConstant.TUBE_PREGNANCY_RESULT, name = "怀孕自测预测结果页面")
class PregnancyResultActivity :
    DataBindingActivity<PregnancyViewModel, ActivityPregnancyBinding>() {

    private val pregnancyHelpGroupAdapter by lazy {
        PregnancyHelpGroupAdapter()
    }

    private val forecastAnimatorHelper by lazy {
        ForecastAnimatorHelper()
    }

    var isHideForecast = false

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()
        binding.flForecast.postDelayed({

            if (isHideForecast) {
                forecastAnimatorHelper.stopAnimator()
                showResultUI()
            }
            isHideForecast = true

        }, 2000)

        binding.rvContent.apply {
            addItemDecoration(GridSpacingItemDecoration(spacing = 10.dp, edgeSpacing = 20.dp))
            adapter = pregnancyHelpGroupAdapter
        }
        binding.tvResult.typeface = Typeface.createFromAsset(
            this.assets, "fonts/din_alternate_bold.ttf"
        )


        binding.imageBack.setOnClickListener {
            finish()
        }

    }

    override fun initData() {
        forecastAnimatorHelper.startForecastAnimator2(
            binding.viewBg,
            binding.slView,
            binding.tvForecast
        )
    }

    override fun getData() {
        started {
            //提交预测
            viewModel.pregnancySubmit().netCatch {
                isHideForecast = false
                "获取数据异常".showToast()
            }.next {
                binding.resultBean = this
                binding.executePendingBindings()
                pregnancyHelpGroupAdapter.setList(this.recommendCircle)
                ShapeBuilder()
                    .setShapeGradientStartColor(
                        Color.parseColor(this.cartOne?.bgColor?.leftColor ?: "#FFFCD1BD")
                    )
                    .setShapeCornersRadius(12.dp.toFloat())
                    .setShapeGradientAngle(ShapeGradientAngle.LEFT_RIGHT)
                    .setShapeGradientEndColor(
                        Color.parseColor(this.cartOne?.bgColor?.rightColor ?: "#FFFEF3EE")
                    ).into(binding.clReportOne)
                ShapeBuilder()
                    .setShapeGradientStartColor(
                        Color.parseColor(this.cartTwo?.bgColor?.leftColor ?: "#FFCAEE7E")
                    )
                    .setShapeCornersRadius(12.dp.toFloat())
                    .setShapeGradientAngle(ShapeGradientAngle.LEFT_RIGHT)
                    .setShapeGradientEndColor(
                        Color.parseColor(this.cartTwo?.bgColor?.rightColor ?: "#FFF0F9DF")
                    ).into(binding.clReportTwo)
                binding.ivResult.startResultAnimator()
                if (isHideForecast) {
                    showResultUI()
                }
                isHideForecast = true
            }
        }
    }

    private fun showResultUI() {
        binding.flForecast.gone()
        binding.clResult.visible()
    }

    private fun View.startResultAnimator() {
        val resizeAvenger = AnimatorSet()
        val animResizeX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.07f)
        val animResizeY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.07f)
        resizeAvenger.playTogether(
            animResizeX,
            animResizeY
        )
        resizeAvenger.interpolator = EaseCubicInterpolator()
        resizeAvenger.duration = 800
        resizeAvenger.start()
        resizeAvenger.addListener(onEnd = {
            this.stopResultAnimator()
        })
    }

    private fun View.stopResultAnimator() {
        val resizeAvenger = AnimatorSet()
        val animResizeX = ObjectAnimator.ofFloat(this, "scaleX", 1.07f, 1f)
        val animResizeY = ObjectAnimator.ofFloat(this, "scaleY", 1.07f, 1f)
        resizeAvenger.playTogether(
            animResizeX,
            animResizeY
        )
        resizeAvenger.interpolator = EaseCubicInterpolator()
        resizeAvenger.duration = 800
        resizeAvenger.start()
        resizeAvenger.addListener(onEnd = {
            this.startResultAnimator()
        })
    }
}
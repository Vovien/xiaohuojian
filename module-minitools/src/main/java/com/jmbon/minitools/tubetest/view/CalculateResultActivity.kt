package com.jmbon.minitools.tubetest.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.animation.addListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.base.util.ParamsConstant
import com.jmbon.minitools.databinding.ActivityCalculateBinding
import com.jmbon.minitools.router.MiniToolRouter
import com.jmbon.minitools.tubetest.bean.ForecastResultBean
import com.jmbon.minitools.tubetest.bean.UserInfoBean
import com.jmbon.minitools.tubetest.util.ForecastAnimatorHelper
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.tubetest.viewmodel.TubeViewModel
import com.jmbon.widget.dialog.ExitDialog
import com.jmbon.widget.interpolator.EaseCubicInterpolator
import com.lxj.xpopup.XPopup


@Route(path = TubeArouterConstant.TUBE_CALCULATE_RESULT, name = "试管自测预测结果页")
class CalculateResultActivity : ViewModelActivity<TubeViewModel, ActivityCalculateBinding>() {
    val forecastAnimatorHelper by lazy {
        ForecastAnimatorHelper()
    }

    var isFirst = true
    var isHideForecast = false
    var forecastResultBean: ForecastResultBean? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)


    }

    override fun initView(savedInstanceState: Bundle?) {

        titleBarView.gone()
        titleBarView.leftImageButton.gone()
        titleBarView.setRightView(R.layout.layout_minitool_right)
        titleBarView.rightCustomView.findViewById<ImageView>(R.id.iv_more).imageTintList =
            ColorStateList.valueOf(ColorUtils.getColor(R.color.white))
        titleBarView.rightCustomView.findViewById<ImageView>(R.id.iv_close).imageTintList =
            ColorStateList.valueOf(ColorUtils.getColor(R.color.white))

        titleBarView.rightCustomView.setBackgroundColor(ColorUtils.getColor(R.color.color_currency))
        titleBarView.setBackgroundColor(ColorUtils.getColor(R.color.color_currency))
        titleBarView.leftImageButton.imageTintList =
            ColorStateList.valueOf(ColorUtils.getColor(R.color.white))
        titleBarView.centerTextView.setTextColor(ColorUtils.getColor(R.color.white))
        binding.flForecast.postDelayed({

            if (isHideForecast) {
                forecastAnimatorHelper.stopAnimator()
                showResultUI()
            }
            isHideForecast = true

        }, 2000)




        initListener()

    }

    private fun addCircle() {
        binding.llBottom.post {
            val width = binding.llBottom.width

            //因为有n-1个间隔，所以先减去一个圆，此时空格数和剩下圆相等
            //计算多出的宽度，即不足一个圆的宽度
            val space = (width - 18f.dp()) % 27f.dp()
            val num = (width - 18f.dp()) / 27f.dp()
            var offset = 9f.dp()
            if (space != 0) {
                offset += space / num
            }

            Log.e("TAG", "${width},${space},${num},${offset},${9f.dp()}")
            //先添加一个圆
            val view = createCircleView()
            binding.llBottom.addView(view)


            for (int in 0 until num) {
                val view = createCircleView()
                binding.llBottom.addView(view)
                var layoutParams = view.layoutParams as LinearLayout.LayoutParams
                layoutParams.marginStart = offset
                view.layoutParams = layoutParams
            }
        }
    }

    private fun createCircleView(): View {
        var superView = View(this)
        var layoutParams = LinearLayout.LayoutParams(18f.dp(), 18f.dp())
        superView.layoutParams = layoutParams
        superView.setBackgroundResource(R.drawable.drawable_circle)

        return superView
    }

    private fun initListener() {

        binding.apply {
            btnMore.setOnClickListener {
                forecastResultBean?.let {
                    ARouter.getInstance().build(TubeArouterConstant.TUBE_CALCULATE_DETAIL_RESULT)
                        .withParcelable(ParamsConstant.PARAMS, it).navigation()
                }
            }

            llTry.setOnClickListener {
                showRetryDialog()
            }


            titleBarView.rightCustomView.findViewById<View>(R.id.iv_more).setOnClickListener {
                MiniMenuUtils.showMenuDialog(
                    this@CalculateResultActivity,
                    TubeConstant.miniApp

                )
            }

            titleBarView.rightCustomView.findViewById<View>(R.id.iv_close).setOnClickListener {
                TubeConstant.lastActivity?.let {
                    ActivityUtils.finishToActivity(it, true)
                }
            }


        }
    }

    override fun initData() {
    }

    override fun getData() {
        started {
            //提交预测
            TubeConstant.userInfoBean?.apply {
                viewModel.startForecast(
                    info.getUserInfoStr(),
                    problems,
                    sperms,
                    ovarys,
                    uteruss,
                    fallopians,
                    pregnancy_number,
                    birth_number,
                    tubetest_number,
                    tubetest_type,
                    programme,
                    demands,
                    city_id,
                    consider_tube,
                    tube_city,
                    tube_cost,
                    other_problem,
                    other_ovarys,
                    other_uteruss,
                    other_fallopians,
                    other_demands
                ).netCatch {
                    isHideForecast = false
                    "获取数据异常".showToast()
                }.next {
                    forecastResultBean = this
                    if (isHideForecast) {
                        showResultUI()
                    }
                    isHideForecast = true
                }
            }
        }
    }

    fun UserInfoBean.UserInfo.getUserInfoStr(): String {
        return "${this.ageId},${this.heightId},${this.weightId}"
    }

    fun showResultUI() {
//        binding.flForecast.gone()
//        binding.clResult.visible()
//        titleBarView.visible()
//        titleBarView.setBackgroundColor(ColorUtils.getColor(R.color.color_currency))
//
//        setTitleName(getString(R.string.forecast_result))
//
//        StatusBarCompat.setStatusBarColor(this, ColorUtils.getColor(R.color.color_currency))
//
//        addCircle()
//
//        forecastResultBean?.apply {
//            binding.tvDays.text = result.scheduleDays
//            binding.tvCost.text = result.predictCost
//
//            startResultAnimator()
//        }

        forecastResultBean?.let {
            // 如果从获取案例进入, 则跳转到获取方案结果页
//            if (ActivityUtils.getActivityList()?.find { it1 -> it1.localClassName.contains("GetSchemeDetailActivity") } != null) {
//                MiniToolRouter.toGetSchemeResult(it)
//            } else {
//                MiniToolRouter.toCommonResult(ResultTypeEnum.RESULT_TYPE_TUBE_TEST.value)
//            }
            MiniToolRouter.toGetSchemeResult(it)
            finish()
        }
    }

    private fun ForecastResultBean.startResultAnimator() {
        val floatAnimator = ValueAnimator.ofFloat(0f, result.oneSuccessRate * 100)
        floatAnimator.addUpdateListener {
            val result = it.animatedValue as Float

            //binding.tvResult.text = "${result}%"
            binding.tvResult.text = "${"%.1f".format(result)}%"

        }
        floatAnimator.interpolator = EaseCubicInterpolator()
        floatAnimator.duration = 300
        floatAnimator.start()

        //成功率显示完成开始首次日程动画
        floatAnimator.addListener(onEnd = {
            startDaysAnimator()
        })
    }

    private fun startDaysAnimator() {


        val animTransDaysX = ObjectAnimator.ofFloat(binding.clDays, "translationY", 40f, 0f)
        animTransDaysX.duration = 450
        animTransDaysX.interpolator = EaseCubicInterpolator()
        val animAlphaDays = ObjectAnimator.ofFloat(binding.clDays, "alpha", 0f, 1f)
        animAlphaDays.duration = 450
        animAlphaDays.interpolator = EaseCubicInterpolator()
        val animTransCostX = ObjectAnimator.ofFloat(binding.clCost, "translationY", 40f, 0f)
        animTransCostX.duration = 450
        animTransCostX.interpolator = EaseCubicInterpolator()
        val animAlphaCost = ObjectAnimator.ofFloat(binding.clCost, "alpha", 0f, 1f)
        animAlphaCost.duration = 450
        animAlphaCost.interpolator = EaseCubicInterpolator()
        val animAlphaBtn = ObjectAnimator.ofFloat(binding.btnMore, "alpha", 0f, 1f)
        animAlphaBtn.duration = 300
        animAlphaBtn.interpolator = EaseCubicInterpolator()
        val animAlphaTry = ObjectAnimator.ofFloat(binding.llTry, "alpha", 0f, 1f)
        animAlphaTry.duration = 300
        animAlphaTry.interpolator = EaseCubicInterpolator()


        animTransDaysX.start()
        animAlphaDays.start()

        binding.clCost.postDelayed({
            animTransCostX.start()
            animAlphaCost.start()
        }, 200)

        binding.clCost.postDelayed({
            animAlphaBtn.start()
            animAlphaTry.start()
        }, 600)

    }


    override fun onResume() {
        super.onResume()
        if (isFirst) {
            isFirst = false
            forecastAnimatorHelper.startForecastAnimator2(
                binding.viewBg,
                binding.slView,
                binding.tvForecast
            )
        }
    }

    private fun showRetryDialog() {
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(ExitDialog(this, "确定重新测试吗？", "", "再等等", "确定", {
                ARouter.getInstance().build(TubeArouterConstant.TUBE_INDEX)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                finish()
            }, {}))
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        TubeConstant.lastActivity?.let {
            ActivityUtils.finishToActivity(it, true)
        }

    }
}
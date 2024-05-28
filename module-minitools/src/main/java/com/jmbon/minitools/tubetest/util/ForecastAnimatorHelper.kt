package com.jmbon.minitools.tubetest.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.core.animation.addListener
import com.jmbon.middleware.interpolator.EaseCubicInterpolator


/**
 * @author : leimg
 * time   : 2022/12/9
 * desc   : 预测动画辅助类
 * version: 1.0
 */
class ForecastAnimatorHelper {
    val ANIMATOR_TIME = 1000L

    var resizeAvenger: AnimatorSet? = null
    var resizeReverseAvenger: AnimatorSet? = null

    fun startForecastAnimator(bgView: View, slView: View, tvView: TextView) {
        resizeAvenger = AnimatorSet()

        //背景大小变化
        val animResizeX = ObjectAnimator.ofFloat(bgView, "scaleX", 1f, 1.2f)
        animResizeX.duration = ANIMATOR_TIME


        val animResizeY = ObjectAnimator.ofFloat(bgView, "scaleY", 1f, 1.2f)
        animResizeY.duration = ANIMATOR_TIME

        //背景圆透明度变化
        val animAlphaBg = ObjectAnimator.ofFloat(bgView, "alpha", 1f, 0.5f)
        animAlphaBg.duration = ANIMATOR_TIME

        //预测中文字透明度变化
        val animAlphaText = ObjectAnimator.ofFloat(tvView, "alpha", 1f, 0.6f)
        animAlphaText.duration = ANIMATOR_TIME


        //小圆大小变化
        val animResizeSlX = ObjectAnimator.ofFloat(slView, "scaleX", 1f, 0.98f)
        animResizeSlX.duration = ANIMATOR_TIME

        val animResizeSlY = ObjectAnimator.ofFloat(slView, "scaleY",  1f, 0.98f)
        animResizeSlY.duration = ANIMATOR_TIME

        resizeAvenger?.interpolator = EaseCubicInterpolator()


        resizeAvenger?.playTogether(
            animResizeX,
            animResizeY,
            animAlphaBg,
            animAlphaText,
            animResizeSlX,
            animResizeSlY
        )
        resizeAvenger?.start()
        resizeAvenger?.addListener (onEnd = {
            startReverseForecastAnimator(bgView, slView, tvView)
        })
    }

    fun startReverseForecastAnimator(bgView: View, slView: View, tvView: TextView) {
        resizeReverseAvenger = AnimatorSet()

        //背景大小变化
        val animResizeX = ObjectAnimator.ofFloat(bgView, "scaleX", 1.2f, 1f)
        animResizeX.duration = ANIMATOR_TIME
        val animResizeY = ObjectAnimator.ofFloat(bgView, "scaleY", 1.2f, 1f)
        animResizeY.duration = ANIMATOR_TIME

        //背景圆透明度变化
        val animAlphaBg = ObjectAnimator.ofFloat(bgView, "alpha", 0.5f, 1f)
        animResizeY.duration = ANIMATOR_TIME

        //预测中文字透明度变化
        val animAlphaText = ObjectAnimator.ofFloat(tvView, "alpha", 0.6f, 1f)
        animResizeY.duration = ANIMATOR_TIME

        //小圆大小变化
        val animResizeSlX = ObjectAnimator.ofFloat(slView, "scaleX", 0.98f, 1f)
        animResizeX.duration = ANIMATOR_TIME
        val animResizeSlY = ObjectAnimator.ofFloat(slView, "scaleY", 0.98f, 1f)
        animResizeY.duration = ANIMATOR_TIME
        resizeReverseAvenger?.interpolator = EaseCubicInterpolator()
        resizeReverseAvenger?.playTogether(
            animResizeX,
            animResizeY,
            animAlphaBg,
            animAlphaText,
            animResizeSlX,
            animResizeSlY
        )
        resizeReverseAvenger?.start()

        resizeReverseAvenger?.addListener (onEnd = {
            startForecastAnimator(bgView, slView, tvView)
        })
//        animResizeX.addListener(onEnd = {
//
//        })
    }

    fun stopAnimator() {
        resizeAvenger?.end()
        resizeReverseAvenger?.end()
    }


    fun startForecastAnimator2(bgView: View, slView: View, tvView: TextView) {
       resizeAvenger = AnimatorSet()

        //背景大小变化
        val animResizeX = ObjectAnimator.ofFloat(bgView, "scaleX", 1f, 1.2f)
        animResizeX.duration = ANIMATOR_TIME
       // animResizeX.interpolator = EaseCubicInterpolator()
        animResizeX.repeatCount = ValueAnimator.INFINITE
        animResizeX.repeatMode = ValueAnimator.REVERSE


        val animResizeY = ObjectAnimator.ofFloat(bgView, "scaleY", 1f, 1.2f)
        animResizeY.duration = ANIMATOR_TIME
        //animResizeY.interpolator = EaseCubicInterpolator()
        animResizeY.repeatMode = ValueAnimator.REVERSE
        animResizeY.repeatCount = ValueAnimator.INFINITE
        //背景圆透明度变化
        val animAlphaBg = ObjectAnimator.ofFloat(bgView, "alpha", 1f, 0.5f)
        animAlphaBg.duration = ANIMATOR_TIME
        //animAlphaBg.interpolator = EaseCubicInterpolator()
        animAlphaBg.repeatMode = ValueAnimator.REVERSE
        animAlphaBg.repeatCount = ValueAnimator.INFINITE
        //预测中文字透明度变化
        val animAlphaText = ObjectAnimator.ofFloat(tvView, "alpha", 1f, 0.6f)
        animAlphaText.duration = ANIMATOR_TIME
        //animAlphaText.interpolator = EaseCubicInterpolator()
        animAlphaText.repeatMode = ValueAnimator.REVERSE
        animAlphaText.repeatCount = ValueAnimator.INFINITE

        //小圆大小变化
        val animResizeSlX = ObjectAnimator.ofFloat(slView, "scaleX", 1f, 0.98f)
        animResizeSlX.duration = ANIMATOR_TIME
        //animResizeSlX.interpolator = EaseCubicInterpolator()
        animResizeSlX.repeatMode = ValueAnimator.REVERSE
        animResizeSlX.repeatCount = ValueAnimator.INFINITE
        val animResizeSlY = ObjectAnimator.ofFloat(slView, "scaleY", 1f, 1f, 0.98f)
        animResizeSlY.duration = ANIMATOR_TIME
        //animResizeSlY.interpolator = EaseCubicInterpolator()
        animResizeSlY.repeatMode = ValueAnimator.REVERSE
        animResizeSlY.repeatCount = ValueAnimator.INFINITE

        resizeAvenger?.interpolator = LinearInterpolator()
        resizeAvenger?.playTogether(
            animResizeX,
            animResizeY,
            animAlphaBg,
            animAlphaText,
            animResizeSlX,
            animResizeSlY
        )
       resizeAvenger?.start()


    }

}
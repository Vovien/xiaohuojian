package com.jmbon.middleware.animator;

import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.jmbon.middleware.interpolator.EaseCubicInterpolator;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 缩放透明
 * Create by dance, at 2018/12/9
 */
public class CustomPopScaleAlphaAnimator extends PopupAnimator {
    public CustomPopScaleAlphaAnimator(View target, int animationDuration, PopupAnimation popupAnimation) {
        super(target, animationDuration, popupAnimation);

    }

    float startScale = .0f;
    float startScale2 = .95f;

    @Override
    public void initAnimator() {
        targetView.setScaleX(startScale);
        targetView.setScaleY(startScale);
        targetView.setAlpha(0);

        // 设置动画参考点
        targetView.post(new Runnable() {
            @Override
            public void run() {
                applyPivot();
            }
        });
    }

    /**
     * 根据不同的PopupAnimation来设定对应的pivot
     */
    private void applyPivot() {
        switch (popupAnimation) {
            case ScaleAlphaFromCenter:
                targetView.setPivotX(targetView.getMeasuredWidth() / 2f);
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
            case ScaleAlphaFromLeftTop:
                targetView.setPivotX(0);
                targetView.setPivotY(0);
                break;
            case ScaleAlphaFromRightTop:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(0f);
                break;
            case ScaleAlphaFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
            case ScaleAlphaFromRightBottom:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
        }

    }

    @Override
    public void animateShow() {
        targetView.post(new Runnable() {
            @Override
            public void run() {
                targetView.animate().scaleX(1f).scaleY(1f).alpha(1f)
                        .setDuration(animationDuration)
                        .setInterpolator(new EaseCubicInterpolator())
//                .withLayer() 在部分6.0系统会引起crash
                        .start();
            }
        });
    }

    @Override
    public void animateDismiss() {
        if (animating) return;
        observerAnimator(targetView.animate().scaleX(startScale2).scaleY(startScale2).alpha(0f).setDuration(animationDuration)
                .setInterpolator(new FastOutSlowInInterpolator()))
//                .withLayer() 在部分6.0系统会引起crash
                .start();
    }

}

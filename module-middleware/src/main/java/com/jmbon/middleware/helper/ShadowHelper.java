package com.jmbon.middleware.helper;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/******************************************************************************
 * Description: 阴影实现类
 *
 * Author: jhg
 *
 * Date: 2023/6/29
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
public class ShadowHelper extends Drawable {

    /**
     * 背景的形状, 支持圆角矩形和圆形
     */
    private int mShape;
    /**
     * 背景圆角
     */
    private int mShapeRadius;
    /**
     * 背景圆角, 可单独设置部分圆角
     */
    private float[] mShapeRadii;
    /**
     * 背景颜色, 可设置纯色, 也可设置渐变色
     */
    private int mBgColor[];
    /**
     * 渐变方向, 默认从左到右
     */
    private int gradientAngle;
    /**
     * View的Padding, 控制阴影的显示范围
     */
    private int[] mViewPadding;

    /**
     * 背景&阴影区域
     */
    private RectF  mRect;
    /**
     * 阴影画笔
     */
    private Paint mShadowPaint;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;

    private ShadowHelper(int shape, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int[] viewPadding) {
        this.mShape = shape;
        this.mBgColor = bgColor;
        this.mShapeRadius = shapeRadius;
        this.mViewPadding = viewPadding;

        int offsetX = 0;
        if (viewPadding[0] != viewPadding[2]) {
            offsetX = viewPadding[2] - (viewPadding[0] + viewPadding[2]) / 2;
        }
        int offsetY = 0;
        if (viewPadding[1] != viewPadding[3]) {
            offsetY = viewPadding[3] - (viewPadding[1] + viewPadding[3]) / 2;
        }
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.TRANSPARENT);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
        mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        if (mBgColor != null) {
            if (mBgColor.length == 1) {
                mBgPaint.setColor(mBgColor[0]);
            } else {
                mBgPaint.setShader(new LinearGradient(mRect.left, mRect.height() / 2, mRect.right,
                        mRect.height() / 2, mBgColor, null, Shader.TileMode.CLAMP));
            }
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRect = new RectF(left + mViewPadding[0], top + mViewPadding[1], right - mViewPadding[2],
                bottom - mViewPadding[3]);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mShape == RECTANGLE) {
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mShadowPaint);
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBgPaint);
        } else {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mShadowPaint);
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mBgPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Builder {
        private int mShape;
        private int mShapeRadius;
        private int mShadowColor;
        private int mShadowRadius;
        private int[] mBgColor;
        private int[] mViewPadding;

        public Builder() {
            mShape = RECTANGLE;
            mShapeRadius = 12;
            mShadowColor = Color.parseColor("#4d000000");
            mShadowRadius = 18;
            mBgColor = new int[1];
            mBgColor[0] = Color.TRANSPARENT;
            mViewPadding = new int[]{0, 0, 0, 0};
        }

        public Builder setShape(int mShape) {
            this.mShape = mShape;
            return this;
        }

        public Builder setShapeRadius(int ShapeRadius) {
            this.mShapeRadius = ShapeRadius;
            return this;
        }

        public Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        public Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        public Builder setBgColor(int BgColor) {
            this.mBgColor[0] = BgColor;
            return this;
        }

        public Builder setBgColor(int[] BgColor) {
            this.mBgColor = BgColor;
            return this;
        }

        public ShadowHelper builder() {
            return new ShadowHelper(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mViewPadding);
        }

        public void applyTo(View targetView) {
            targetView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mViewPadding = new int[]{targetView.getPaddingLeft(), targetView.getPaddingTop(), targetView.getPaddingRight(), targetView.getPaddingBottom()};
            targetView.setBackground(new ShadowHelper(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mViewPadding));
        }
    }
}
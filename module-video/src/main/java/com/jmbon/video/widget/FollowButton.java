package com.jmbon.video.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.SizeUtils;
import com.jmbon.video.R;

/**
 * usage: fake Tiktok follow animation
 * author: kHRYSTAL
 * create time: 2020-04-10
 * update time:
 * email: 723526676@qq.com
 */
public class FollowButton extends View {


    private float radius;
    private RectF rect;

    private Paint rectPaint;

    private Paint addPaint;
    private float addLineWidth;
    private Path addPath;

    private Paint successPaint;
    private float successLineWidth;
    private Path successPath;

    private Paint borderPaint;
    private float borderStrokeWidth;
    private Path borderPath = new Path();

    private PathMeasure pathMeasure;
    private boolean startDrawnSuccess;

    @ColorInt
    private int addLineColor;
    @ColorInt
    private int successLineColor;
    @ColorInt
    private int addBackgroundColor;
    @ColorInt
    private int successBackgroundColor;
    @ColorInt
    private int borderColor;

    private float leftOffset;
    private float topOffset;

    private AnimatorSet animationCollection;

    public FollowButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public FollowButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FollowButton);
        radius = ta.getDimension(R.styleable.FollowButton_fb_radius, 0f);
        addLineWidth = ta.getDimension(R.styleable.FollowButton_fb_add_line_width, SizeUtils.dp2px(3f));
        successLineWidth = ta.getDimension(R.styleable.FollowButton_fb_success_line_width, SizeUtils.dp2px(4));
        addLineColor = ta.getColor(R.styleable.FollowButton_fb_add_line_color, 0xFFFFFFFF);
        addBackgroundColor = ta.getColor(R.styleable.FollowButton_fb_add_background, 0xFFEE0051);
        successLineColor = ta.getColor(R.styleable.FollowButton_fb_success_line_color, 0xFFEE0051);
        successBackgroundColor = ta.getColor(R.styleable.FollowButton_fb_success_background, 0xFFFFFFFF);
        leftOffset = ta.getDimension(R.styleable.FollowButton_fb_left_offset, 0f);
        topOffset = ta.getDimension(R.styleable.FollowButton_fb_top_offset, 0f);
        borderStrokeWidth = ta.getDimension(R.styleable.FollowButton_fb_border_width, 0f);
        borderColor = ta.getColor(R.styleable.FollowButton_fb_border_color, 0xFFF);
        ta.recycle();
        initView();
    }

    private void initView() {
        rect = new RectF();
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(addBackgroundColor);

        addPaint = new Paint();
        addPaint.setAntiAlias(true);
        addPaint.setColor(addLineColor);
        addPaint.setStyle(Paint.Style.STROKE);
        addPaint.setStrokeCap(Paint.Cap.ROUND);
        addPaint.setStrokeWidth(addLineWidth);

        successPaint = new Paint();
        successPaint.setAntiAlias(true);
        successPaint.setColor(successLineColor);
        successPaint.setStyle(Paint.Style.STROKE);
        successPaint.setStrokeCap(Paint.Cap.ROUND);
        successPaint.setStrokeWidth(successLineWidth);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderStrokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createBorderPath(w, h);
        createPathBySize();
    }

    private void createBorderPath(int w, int h) {
        int left = (int) borderStrokeWidth;
        int top = (int) borderStrokeWidth;
        int right = (int) (left + w - borderStrokeWidth * 2);
        int bottom = (int) (top + h - borderStrokeWidth * 2);
        int d = bottom - top;
        rect.set(left, top, right, bottom);
        borderPath.reset(); // notice
        borderPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        int l = (int) (d / 2.0f);
        borderPath.moveTo(left + l, top);
        borderPath.lineTo(right - l, top);

        borderPath.moveTo(left + l, bottom);
        borderPath.lineTo(right - l, bottom);

        borderPath.moveTo(left, top + l);
        borderPath.lineTo(left, bottom - l);

        borderPath.moveTo(right, top + l);
        borderPath.lineTo(right, bottom - l);
    }

    private void createPathBySize() {
        // 画加号
        addPath = new Path();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        addPath.moveTo(width * 0.5f + leftOffset, height * 0.28f + topOffset);
        addPath.lineTo(width * 0.5f + leftOffset, height * 0.72f + topOffset);
        addPath.moveTo(width * 0.28f + leftOffset, height * 0.5f + topOffset);
        addPath.lineTo(width * 0.72f + leftOffset, height * 0.5f + topOffset);

        // 画对勾
        successPath = new Path();
        successPath.moveTo(width * 0.26f + leftOffset, height * 0.49f + topOffset);
        successPath.lineTo(width * 0.43f + leftOffset, height * 0.66f + topOffset);
        successPath.lineTo(width * 0.76f + leftOffset, height * 0.37f + topOffset);

        pathMeasure = new PathMeasure(successPath, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (startDrawnSuccess) {
            rectPaint.setColor(successBackgroundColor);
            canvas.drawRoundRect(rect, radius, radius, rectPaint);
            canvas.drawPath(successPath, successPaint);
        } else {
            rectPaint.setColor(addBackgroundColor);
            canvas.drawRoundRect(rect, radius, radius, rectPaint);
            canvas.drawPath(addPath, addPaint);
        }
        canvas.drawPath(borderPath, borderPaint);
    }

    private void createAnimationCollectionIfNull(final boolean reset) {
        if (animationCollection == null || reset) {
            animationCollection = new AnimatorSet();

            // add rotate
            setPivotX(getMeasuredWidth() >> 1);
            setPivotY(getMeasuredHeight() >> 1);
            ValueAnimator addRotateScaleAnimation = ValueAnimator.ofFloat(0f, 90f);
            addRotateScaleAnimation.setDuration(300);
            addRotateScaleAnimation.addUpdateListener(valueAnimator -> {
                float currentValue = (float) valueAnimator.getAnimatedValue();
                setRotation(currentValue);
                setAlpha(1f - currentValue / 90f);
            });

            // draw success step by step
            ValueAnimator successStepAnimation = ValueAnimator.ofFloat(1f, 0f);
            successStepAnimation.setDuration(800);
            successStepAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    startDrawnSuccess = true;
                    invalidate();
                    setAlpha(1f);
                    setRotation(0f);
                }
            });
            successStepAnimation.addUpdateListener(valueAnimator -> {
                float currentValue = (float) valueAnimator.getAnimatedValue();
                PathEffect effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, currentValue * pathMeasure.getLength());
                successPaint.setPathEffect(effect);
                invalidate();
            });

            // stay moment
            ValueAnimator stayAnimation = ValueAnimator.ofFloat(0f, 1f);
            stayAnimation.setDuration(500);

            // disappear
            ValueAnimator successDoneAnimation = ValueAnimator.ofFloat(1f, 0f);
            successDoneAnimation.setDuration(500);
            successDoneAnimation.addUpdateListener(valueAnimator -> {
                float currentValue = (float) valueAnimator.getAnimatedValue();
                setAlpha(valueAnimator.getAnimatedFraction());
                setScaleX(currentValue);
                setScaleY(currentValue);
            });
            successDoneAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    startDrawnSuccess = false;
                    setVisibility(INVISIBLE);
                }
            });
            animationCollection.playSequentially(addRotateScaleAnimation, successStepAnimation, stayAnimation, successDoneAnimation);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void start() {
        if (isAnimating()) {
            return;
        }
        reset();
        animationCollection.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resume() {
        createAnimationCollectionIfNull(false);
        animationCollection.resume();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void reset() {
        pause();
        setVisibility(VISIBLE);
        pathMeasure = new PathMeasure(successPath, true);
        createAnimationCollectionIfNull(true);
        animationCollection.cancel();
        startDrawnSuccess = false;
        setScaleX(1f);
        setScaleY(1f);
        setAlpha(1f);
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pause() {
        createAnimationCollectionIfNull(false);
        animationCollection.pause();
    }

    public boolean isAnimating() {
        createAnimationCollectionIfNull(false);
        return animationCollection.isRunning();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reverse() {
        createAnimationCollectionIfNull(false);
        animationCollection.reverse();
    }

    public void cancel() {
        createAnimationCollectionIfNull(false);
        animationCollection.cancel();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public float getAddLineWidth() {
        return addLineWidth;
    }

    public void setAddLineWidth(float addLineWidth) {
        this.addLineWidth = addLineWidth;
        addPaint.setStrokeWidth(addLineWidth);
    }

    public float getSuccessLineWidth() {
        return successLineWidth;
    }

    public void setSuccessLineWidth(float successLineWidth) {
        this.successLineWidth = successLineWidth;
        successPaint.setStrokeWidth(successLineWidth);
    }

    public int getAddLineColor() {
        return addLineColor;
    }

    public void setAddLineColor(int addLineColor) {
        this.addLineColor = addLineColor;
        addPaint.setColor(addLineColor);
    }

    public int getSuccessLineColor() {
        return successLineColor;
    }

    public void setSuccessLineColor(int successLineColor) {
        this.successLineColor = successLineColor;
        successPaint.setColor(successLineColor);
    }

    public int getAddBackgroundColor() {
        return addBackgroundColor;
    }

    public void setAddBackgroundColor(int addBackgroundColor) {
        this.addBackgroundColor = addBackgroundColor;
    }

    public int getSuccessBackgroundColor() {
        return successBackgroundColor;
    }

    public void setSuccessBackgroundColor(int successBackgroundColor) {
        this.successBackgroundColor = successBackgroundColor;
    }

    public void setLeftOffset(float leftOffset) {
        this.leftOffset = leftOffset;
        createPathBySize();
    }

    public void setTopOffset(float topOffset) {
        this.topOffset = topOffset;
        createPathBySize();
    }

    public long getDuration() {
        createAnimationCollectionIfNull(false);
        return animationCollection.getDuration();
    }
}
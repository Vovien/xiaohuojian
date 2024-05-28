package com.jmbon.minitools.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.jmbon.minitools.R;

public class UnderLineTextView extends AppCompatTextView {

    private Rect mRect;
    private Paint mPaint;
    private int mColor;
    private float density;
    private float mStrokeWidth;
    private float mLineTopMargin = 0;

    public UnderLineTextView(Context context) {
        this(context, null, 0);
    }

    public UnderLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取屏幕密度
        density = context.getResources().getDisplayMetrics().density;
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UnderlinedTextView, defStyleAttr, 0);
        mColor = array.getColor(R.styleable.UnderlinedTextView_underlineColor, 0xFFFF0000);
        mStrokeWidth = array.getDimension(R.styleable.UnderlinedTextView_underlineWidth, density * 1);
        mLineTopMargin = array.getDimension(R.styleable.UnderlinedTextView_underlineTopMargin, density * 2);

        setLineSpacing(mLineTopMargin, (float) 1.5);
        setPadding(getLeft(), getTop(), getRight(), getBottom());

        array.recycle();


        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        //意思是所画虚线规则是先画15个长度的实线，留下5个长度的空白
        //数组规则：虚、实、虚、实---先虚再实，每个数代表长度
        PathEffect effects = new DashPathEffect(new float[]{25, 10}, 0);
        mPaint.setPathEffect(effects);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //得到TextView显示有多少行
        int count = getLineCount();
        //得到TextView的布局
        final Layout layout = getLayout();


        float x_start, x_stop, x_diff;
        int firstCharInLine, lastCharInLine;

        //最后一行不需要下划线-1
        for (int i = 0; i < count-1; i++) {

            //getLineBounds得到这一行的外包矩形,这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
            int baseline = getLineBounds(i, mRect);
            // mRect.bottom+=mLineTopMargin;
            firstCharInLine = layout.getLineStart(i);
            lastCharInLine = layout.getLineEnd(i);

            //要得到这个字符的左边X坐标 用layout.getPrimaryHorizontal
            //得到字符的右边X坐标用layout.getSecondaryHorizontal
            x_start = layout.getPrimaryHorizontal(firstCharInLine);
            x_diff = layout.getPrimaryHorizontal(firstCharInLine + 1) - x_start;
            x_stop = layout.getPrimaryHorizontal(lastCharInLine - 1) + x_diff;

            //canvas.drawLine(x_start,baseline+  mStrokeWidth,x_stop, baseline + mStrokeWidth, mPaint);
            canvas.drawLine(x_start, baseline + mLineTopMargin + mStrokeWidth, x_stop, baseline + mLineTopMargin + mStrokeWidth, mPaint);
            //canvas.drawRect(x_start,baseline + mStrokeWidth,x_stop, baseline + mStrokeWidth, mPaint);


        }
        super.onDraw(canvas);

    }

    public int getUnderLineColor() {
        return mColor;
    }

    public void setUnderLineColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom + (int) mLineTopMargin + (int) mStrokeWidth);
    }

    public float getUnderlineWidth() {
        return mStrokeWidth;
    }

    public void setUnderlineWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

}
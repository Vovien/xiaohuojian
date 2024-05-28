package com.jmbon.minitools.base.widget.ruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmbon.minitools.R;

/**
 * Created to : RulerView的载体.
 * GitHub -> https://github.com/WangcWj/AndroidScrollRuler
 * 提交issues联系作者.
 *
 * @author WANG
 * @date 2019/3/21
 */
public class ScrollRulerLayout extends ViewGroup {
    protected Context mContext;
    private RulerView mRulerView;
    private View mCenterPointer;
    private Paint mLinePaint;
    private int mLineWidth;
    private int mPadding;
    private int mLineSpace;//刻度间隔
    private int mStartLineIndex;//起始刻度位置
    private int mShortLineHeight;//短刻度高度
    private int mLongLineHeight;//长刻度高度
    private int mSmallSpaceNum;//俩个长刻度间短刻度个数
    private boolean mIsFloat = false;//是否支持浮点刻度
    private int mLineColor = Color.GRAY;//刻度线颜色
    private int mTextColor = Color.BLACK;//刻度文本颜色

    public ScrollRulerLayout(Context context) {
        this(context, null);
    }

    public ScrollRulerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollRulerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initAttrs(attrs);

        init(context);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.ScrollRulerLayout);
        try {
            mLineWidth = (int) ta.getDimension(R.styleable.ScrollRulerLayout_rl_line_width, dp2px(1));
            mLineSpace = (int) ta.getDimension(R.styleable.ScrollRulerLayout_rl_line_space, dp2px(8));
            mShortLineHeight = (int) ta.getDimension(R.styleable.ScrollRulerLayout_rl_short_line_height, dp2px(16));
            mLongLineHeight = (int) ta.getDimension(R.styleable.ScrollRulerLayout_rl_long_line_height, dp2px(24));
            mSmallSpaceNum = ta.getInt(R.styleable.ScrollRulerLayout_rl_small_space_num, 10);
            mIsFloat = ta.getBoolean(R.styleable.ScrollRulerLayout_rl_is_float, false);
            mLineColor = ta.getColor(R.styleable.ScrollRulerLayout_rl_line_color, Color.GRAY);
            mTextColor = ta.getColor(R.styleable.ScrollRulerLayout_rl_text_color, Color.BLACK);
            mStartLineIndex =  ta.getInt(R.styleable.ScrollRulerLayout_rl_start_line_index, -1);
        } finally {
            ta.recycle();
        }
    }

    private void init(Context context) {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(mLineColor);
        mPadding = dp2px(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null != mRulerView) {
            mRulerView.measure(widthMeasureSpec, heightMeasureSpec);
        }
        if (null != mCenterPointer) {
            LayoutParams layoutParams = mCenterPointer.getLayoutParams();
            mCenterPointer.measure(layoutParams.width, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (null != mRulerView) {
            MarginLayoutParams lp = (MarginLayoutParams) mRulerView.getLayoutParams();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = getPaddingRight() + getMeasuredWidth();
            int bottom = getPaddingBottom() + getMeasuredHeight() - mLineWidth;
            mRulerView.layout(left, top, right, bottom);
        }
        if (null != mCenterPointer) {
            int measuredWidth = mCenterPointer.getMeasuredWidth();
            int width = getWidth() + getPaddingLeft() + getPaddingRight();
            int left = width / 2 - measuredWidth / 2;
            int right = width / 2 + measuredWidth / 2;
            //中间指示器的固定位置
            mCenterPointer.layout(left, 0, right, getHeight()/2+dp2px(18));
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //不绘制横向X轴坐标
        //  canvas.drawLine(0, getHeight(), getWidth(), getHeight(), mLinePaint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRulerView = new RulerView(getContext(), mLineSpace, mLineWidth, mShortLineHeight, mLongLineHeight, mSmallSpaceNum, mLineColor, mTextColor, mIsFloat,mStartLineIndex);
        mCenterPointer = LayoutInflater.from(mContext).inflate(R.layout.view_ruler_layout, null,false);
        MarginLayoutParams layoutParams = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = mPadding;
        layoutParams.rightMargin = mPadding;
        mRulerView.setLayoutParams(layoutParams);
        layoutParams.width = dp2px(12);
        layoutParams.height = LayoutParams.MATCH_PARENT;
        mCenterPointer.setLayoutParams(layoutParams);
        //mRulerView.setScrollSelected(this);
        addView(mRulerView);
        addView(mCenterPointer);
    }

    public void setScope(int start, int end, int offSet) {
        if (null != mRulerView) {
            mRulerView.setScope(start, end, offSet);
        }
    }


    public RulerView getmRulerView() {
        return mRulerView;
    }

    public void setRulerViewMargin(int left, int top, int right, int bottom) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mRulerView.getLayoutParams();
        setRulerViewMargin(layoutParams, left, top, right, bottom);
    }

    public void setCurrentItem(String text) {
        if (null != mRulerView) {
            mRulerView.setCurrentItem(text);
        }
    }

    public void setRulerViewMargin(MarginLayoutParams layoutParams, int left, int top, int right, int bottom) {
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        layoutParams.rightMargin = right;
        layoutParams.bottomMargin = bottom;
        mRulerView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDetachedFromWindow() {
        mRulerView.destroy();
        mRulerView = null;
        mCenterPointer = null;
        super.onDetachedFromWindow();
    }

    private int dp2px(float dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

}

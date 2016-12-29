package com.renren.customviewstudy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by wuyinlei on 2016/12/29.
 */

public class CustomTitleView extends View {

    private String mTitleText;  //文字

    private int mTitleTextColor; // 文字颜色

    private int mTitleTextSize; // 文字大小

    private Paint mPaint;  //画笔

    private Rect mRect;  //  矩形

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取我们自定义的样式属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int arr = typedArray.getIndex(i); //获取单个属性值
            switch (arr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = typedArray.getString(arr);
                    if (TextUtils.isEmpty(mTitleText))
                        mTitleText = "Hello World";  //默认显示Hello World (如果布局里面没有写入的话)
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    //字体颜色默认是黑色
                    mTitleTextColor = typedArray.getColor(arr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    //默认设置字体的大小16dp  TypedValue也可以把sp转换为px
                    mTitleTextSize = typedArray.getDimensionPixelSize(arr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle(); //资源回收


        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        // mPaint.setColor(mTitleTextColor);
        mRect = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleText = randomText();
                postInvalidate(); //  如果调用的是这个方法,那么初次显示的是多长的字符串,那么背景的大小
                //就是onMeasure的时候计算出来的首次的字符串的长度,而在次被点击的时候,只会去调用View的onDraw方法
                //而不会再次调用onMeasure方法,所以宽度自定义view的宽度并不会因为字符串的长度的改变而改变

                //requestLayout();  //  如果想要改变字符串长度之后去改变自定义view的宽度,那么就可以调用左边的这个方法
                //这个方法会重新走onMeasure方法,重新计算字符串的宽度,然后改变自定义view的宽度
            }
        });

    }

    /**
     * 随机生成四个数字
     *
     * @return 四个数字
     */
    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer integer : set) {
            sb.append("" + integer);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textWidth = mRect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        mPaint.setAntiAlias(true);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        // mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2 - mRect.left, getHeight() / 2 + mRect.height() / 2, mPaint);
    }
}

package com.renren.customviewstudy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.renren.customviewstudy.R;


/**
 * 自定义ImageView(添加描述) 简单的
 * 1、自定义属性
 * 2、获取自定义属性
 * 3、onMeasure测量大小
 * 4、onDraw绘制
 */
public class CustomImageView extends View {

    private Bitmap mBitmap; //图片

    private int mImageScaleType; //图片缩放格式

    private String mTitleText; //文字

    private int mTitleTextSize;// 文字大小

    private int mTitleTextColor;//文字颜色

    private Rect mRect;// 用来画图片使用

    private Rect mTextBound; // 用来画文字使用

    private Paint mPaint;//画笔

    private int mWidth; //宽

    private int mHeight;//高

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        //获取自定义的属性个数
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int arr = array.getIndex(i);
            switch (arr) {
                case R.styleable.CustomImageView_image:  //得到设置的图片
                    mBitmap = BitmapFactory.decodeResource(getResources(), array.getResourceId(arr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType: //得到设置图片的type
                    mImageScaleType = array.getInt(array.getIndex(arr), 0);
                    break;
                case R.styleable.CustomImageView_titleText: //得到图片的描述
                    mTitleText = array.getString(arr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:  //得到文字描述的颜色
                    mTitleTextColor = array.getColor(array.getIndex(arr), 0);
                    break;
                case R.styleable.CustomImageView_titleTextSize: //文字的大小
                    mTitleTextSize = array.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()
                    ));
                    break;
            }
        }

        array.recycle();

        mRect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        mPaint.setTextSize(mTitleTextSize);

        //防止如果没有设置text造成异常
        if (!TextUtils.isEmpty(mTitleText)) {
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        /**
         * 设置宽度
         */
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else {
            //由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mBitmap.getWidth();
            //由字体决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextBound.width();

            if (specMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.min(desire, specSize);
            }
        }

        /**
         * 设置高度
         */

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mBitmap.getHeight() + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desire, specSize);
            }
        }
        Log.d("CustomImageView", "mWidth: " + mWidth + "    mHeight: " + mHeight);

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //

        /**
         * 绘制边框
         */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTitleTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx
         */
        if (mTextBound.width() > mWidth) {
            @SuppressLint("DrawAllocation") TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText, paint,
                    (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();

            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            //正常情况
            canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() * 1.0f / 2,
                    mHeight - getPaddingBottom(), mPaint);
        }
        //取消使用掉
        mRect.bottom -= mTextBound.height();

        int IMAGE_SCALE_FITXY = 0;
        if (mImageScaleType == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mBitmap, null, mRect, mPaint);
        } else {
            //计算居中的矩形的范围
            mRect.left = mWidth / 2 - mBitmap.getWidth() / 2;
            mRect.right = mWidth / 2 + mBitmap.getWidth() / 2;
            mRect.top = (mHeight - mTextBound.height()) / 2 - mBitmap.getHeight() / 2;
            mRect.bottom = (mHeight - mTextBound.height()) / 2 + mBitmap.getHeight() / 2;

            canvas.drawBitmap(mBitmap,null,mRect,mPaint);
        }

    }
}

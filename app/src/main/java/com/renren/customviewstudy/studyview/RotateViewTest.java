package com.renren.customviewstudy.studyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wuyinlei on 2016/12/30.
 */

public class RotateViewTest extends View {
    public RotateViewTest(Context context) {
        this(context,null);
    }

    public RotateViewTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RotateViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(70);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为rotate之前所化",20,80,paint);

        canvas.rotate(50);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为rotate之后所化",20,80,paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}

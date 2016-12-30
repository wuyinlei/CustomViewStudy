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

public class TranslateViewTest extends View {
    public TranslateViewTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TranslateViewTest(Context context) {
        this(context,null);
    }

    public TranslateViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(70);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为translte之前所化",20,80,paint);

        canvas.translate(100,300);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为translte之后所化",20,80,paint);
    }
}

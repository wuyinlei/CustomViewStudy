package com.renren.customviewstudy.studyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wuyinlei on 2016/12/30.
 */

public class ClipViewTest extends View {
    public ClipViewTest(Context context) {
        this(context,null);
    }

    public ClipViewTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为Canvas裁剪之前所绘制",20,80,paint);
        Rect rect = new Rect(20,200,900,1000);
        canvas.clipRect(rect);
        canvas.drawColor(Color.GREEN);
       // canvas.rotate(50);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为canvas裁剪之后所绘制",10,300,paint);
    }
}

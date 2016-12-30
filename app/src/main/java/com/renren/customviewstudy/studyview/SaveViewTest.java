package com.renren.customviewstudy.studyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class SaveViewTest extends View {
    public SaveViewTest(Context context) {
        this(context,null);
    }

    public SaveViewTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SaveViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
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
        canvas.save();

        Rect rect = new Rect(20,200,900,1000);
        canvas.clipRect(rect);
        canvas.drawColor(Color.GREEN);
        // canvas.rotate(50);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为canvas裁剪之后所绘制",10,300,paint);
        canvas.restore();

        paint.setColor(Color.RED);
        canvas.drawText("红色字体为Canvas恢复裁剪之后所绘制",20,170,paint);
    }
}

package com.renren.customviewstudy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_meizi);
        bitmap = getRoundCornerImage(bitmap, 80);
        mImageView.setImageBitmap(bitmap);

    }

    /**
     * @param bitmap 原图片
     * @param pixels 角度
     * @return 带圆角的图片
     */
    private Bitmap getRoundCornerImage(Bitmap bitmap, float pixels) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap roundCornerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);


        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        paint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return roundCornerBitmap;
    }
}

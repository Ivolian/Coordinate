package com.unicorn.coordinate.profile.matchCert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class UnderlineTextView extends AppCompatTextView {

    public UnderlineTextView(Context context) {
        super(context);
        init();
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint paint = new Paint();

    private void init() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
//        canvas.drawColor(Color.WHITE);                  //设置背景颜色
        paint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(0, 0, 100, 0, paint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
    }

}

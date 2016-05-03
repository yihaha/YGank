package com.ybh.lovemeizi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by y on 2016/4/15.
 */
public class YTestView extends View {

    // 颜色表
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636
            , 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    private Paint paint;
    private int mWidth;
    private int mHeight;

    public YTestView(Context context) {
        super(context);
    }

    public YTestView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}

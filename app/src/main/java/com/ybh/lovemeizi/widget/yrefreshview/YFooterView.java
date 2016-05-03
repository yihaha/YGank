package com.ybh.lovemeizi.widget.yrefreshview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by y on 2016/4/19.
 */
public class YFooterView extends View {

    private int pull_height;
    private Paint onePaint;
    private boolean isRefreshing;
    private long mStartTime;
    private int mWidth;
    private int mHeight;
    private Paint textPaint;
    private long REFRESH_DUR = 1000;
    public boolean isShowUp=true;

    public YFooterView(Context context) {
        this(context, null);
    }

    public YFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        pull_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());

        onePaint = new Paint();
        onePaint.setAntiAlias(true);
        onePaint.setStyle(Paint.Style.FILL);
        onePaint.setColor(0xff2B2B2B);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(0xffffffff);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(24f);
    }

    private enum AnimationStatus {
        PULL_DOWN_STATUS,
        DROG_DOWN_STATUS,
        REFRESH_STATUS,
    }

    private AnimationStatus mCurrStatus;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();
            if (mHeight < pull_height) {
                mCurrStatus = AnimationStatus.PULL_DOWN_STATUS;
            } else {
                mCurrStatus = AnimationStatus.DROG_DOWN_STATUS;
            }
            if (isRefreshing) {
                mCurrStatus = AnimationStatus.REFRESH_STATUS;
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurrStatus) {
            case PULL_DOWN_STATUS:
                canvas.drawRect(0, 0, mWidth, mHeight, onePaint);
                if (isShowUp) {
                    canvas.drawText("上拉", mWidth / 2, mHeight / 2, textPaint);
                }
                break;
            case DROG_DOWN_STATUS:
                canvas.drawRect(0, 0, mWidth, mHeight, onePaint);
                canvas.drawText("松开加载更多", mWidth / 2, mHeight / 2, textPaint);
                break;
            case REFRESH_STATUS:
                onLoadmore(canvas, getRefreshRation());
                invalidate();
                break;
        }
    }

    private float getRefreshRation() {
        if (isRefreshing) {
            return (System.currentTimeMillis() - mStartTime) % REFRESH_DUR /(float)REFRESH_DUR;
        }
        return 1;
    }

    private void onLoadmore(Canvas canvas, float ration) {
        canvas.drawRect(0, 0, mWidth, mHeight, onePaint);
        int count = (int) (ration * 4);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(".");
        }
        canvas.drawText("正在加载" + stringBuilder.toString(), mWidth / 2, mHeight / 2, textPaint);
    }

    public void setStartRefresh() {
        this.isRefreshing = true;
        this.mCurrStatus = AnimationStatus.REFRESH_STATUS;
        this.mStartTime = System.currentTimeMillis();
        requestLayout();
    }

    public void setRefresh(boolean refresh) {
        this.isRefreshing = refresh;
    }

}

package com.ybh.lovemeizi.widget.yrefreshview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by y on 2016/4/19.
 */
public class YHeaderView extends View {

    private Path mPath;
    private Paint textPaint;
    private Paint twoPaint;
    private Paint onePaint;
    private int mHeight;
    private int mWidth;
    private int pull_height, radius;
    private int x0;
    private int y0;
    private int x1;
    private int y1;
    private double angle1;
    private double angle2;
    private double angle3;

    public YHeaderView(Context context) {
        this(context, null);
    }

    public YHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //背景画笔,用来画矩形
        onePaint = new Paint();
        onePaint.setAntiAlias(true);
        onePaint.setColor(0xff2B2B2B);
        onePaint.setStyle(Paint.Style.FILL);

        //画圆
        twoPaint = new Paint();
        twoPaint.setStyle(Paint.Style.STROKE);
        twoPaint.setStrokeWidth(2F);
        twoPaint.setAntiAlias(true);
        twoPaint.setColor(0xffffffff);

        //画文字
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xffffffff);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(24f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        mPath = new Path(); //路径,

        //刷新时候的高度
        pull_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        //圆半径
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics()) / 2;

    }

    private enum AnimationStatus {
        PULL_DOWN_STATUS,
        DROG_DOWN_STATUS, //下拉到指定高度
        REFRESH_STATUS,
    }

    private AnimationStatus currStatus = AnimationStatus.PULL_DOWN_STATUS;
    private boolean isRefreshing;
    public boolean isShowDown;//isShowDown是判断是否先"下拉"
    private long mStartRefreshTime;
    private long REFRESH_DUR = 10000;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();
           if (mHeight < pull_height) {
                currStatus = AnimationStatus.PULL_DOWN_STATUS;
            } else{
                currStatus = AnimationStatus.DROG_DOWN_STATUS;
            }
            if (isRefreshing) {
                currStatus = AnimationStatus.REFRESH_STATUS;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (currStatus) {
            case PULL_DOWN_STATUS:
                canvas.drawRect(0, 0, mWidth, mHeight, onePaint);
                if (isShowDown) {
                    canvas.drawText("下拉", mWidth / 2, mHeight / 2, textPaint);
                }
                break;
            case DROG_DOWN_STATUS:
                canvas.drawRect(0, 0, mWidth, mHeight, onePaint);
                canvas.drawText("松开开始刷新", mWidth / 2, mHeight / 2, textPaint);
                break;

            case REFRESH_STATUS:
                onRefresh(canvas, getRefrshRation());
                invalidate();
                break;
        }

    }

    private float getRefrshRation() {
        if (isRefreshing) {
            return (System.currentTimeMillis() - mStartRefreshTime) % REFRESH_DUR / (float) REFRESH_DUR;
        }
        return 1;
    }

    /**
     * 刷新动画
     *
     * @param canvas
     * @param refreshRation
     */
    private void onRefresh(Canvas canvas, float refreshRation) {
        canvas.drawRect(0, 0, mWidth, pull_height, onePaint);
        canvas.drawCircle(mWidth / 2, pull_height / 2, radius, twoPaint);

        x0 = mWidth / 2;
        y0 = pull_height / 2;
        angle1 = refreshRation * Math.PI * 24;
        x1 = (int) (x0 + radius * Math.cos(angle1) * 0.9);
        y1 = (int) (y0 + radius * Math.sin(angle1) * 0.9);
        mPath.reset();
        mPath.moveTo(x0, y0);
        mPath.lineTo(x1, y1);
        canvas.drawPath(mPath, twoPaint);

        angle2 = refreshRation * Math.PI * 12;
        x1 = (int) (x0 + radius * Math.cos(angle2) * 0.6);
        y1 = (int) (y0 + radius * Math.sin(angle2) * 0.6);
        mPath.reset();
        mPath.moveTo(x0, y0);
        mPath.lineTo(x1, y1);
        canvas.drawPath(mPath, twoPaint);

        angle3 = refreshRation * Math.PI * 6;
        x1 = (int) (x0 + radius * Math.cos(angle3) * 0.4);
        y1 = (int) (y0 + radius * Math.sin(angle3) * 0.4);
        mPath.reset();
        mPath.moveTo(x0, y0);
        mPath.lineTo(x1, y1);
        canvas.drawPath(mPath, twoPaint);

    }

    public void onStartRefresh() {
        isRefreshing = true;
        currStatus = AnimationStatus.REFRESH_STATUS;
        mStartRefreshTime = System.currentTimeMillis();
        requestLayout();
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefreshing = isRefresh;
    }

}

package com.ybh.lovemeizi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jaeger on 15-7-22.
 * ListenRain
 */
public class HeaderView extends View {

    private TextView test;
    private static int PULL_HEIGHT;

    private boolean isRefresh;

    private int mWidth;
    private int mHeight;

    private Paint mBackPaint;
    private Paint mFrontPain;
    private Paint textPaint;
    private Path mPath;


    public HeaderView(Context context) {
        this(context, null, 0);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        PULL_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics()) / 2;

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(0xFF262626);

        mFrontPain = new Paint();
        mFrontPain.setStyle(Paint.Style.STROKE);
        mFrontPain.setStrokeWidth(3);
        mFrontPain.setAntiAlias(true);
        mFrontPain.setColor(0xFFffffff);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(25f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(0xFFffffff);

        mPath = new Path();

    }

    private AnimatorStatus mStatus = AnimatorStatus.PULL_DOWN;

    private enum AnimatorStatus {
        PULL_DOWN,
        DRAG_DOWN,
        REL_DRAG,
        REFRESH;

        @Override
        public String toString() {
            switch (this) {
                case PULL_DOWN:
                    return "pull down";
                case DRAG_DOWN:
                    return "drag down";
                default:
                    return "unknown state";
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();
            if (mHeight < PULL_HEIGHT) {
                mStatus = AnimatorStatus.PULL_DOWN;
            } else {
                mStatus = AnimatorStatus.DRAG_DOWN;
            }
            if (isRefresh) {
                mStatus = AnimatorStatus.REFRESH;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStatus) {
            case DRAG_DOWN:
                canvas.drawRect(0, 0, mWidth, mHeight, mBackPaint);
                canvas.drawText("放开开始刷新", (float) mWidth / 2, (float) (mHeight / 2), textPaint);
                break;
            case PULL_DOWN:
                canvas.drawRect(0, 0, mWidth, mHeight, mBackPaint);
                canvas.drawText("下拉",(float) mWidth / 2, (float) (mHeight / 2), textPaint);
                break;
//            case DRAG_DOWN:
//                drawDrag(canvas);
//                break;
            case REFRESH:
                drawRefreshing(canvas, getRefRation());
                invalidate();
                break;
            case REL_DRAG:
//                drawRelDrag(canvas);
                break;
        }
    }



    private int radius;
    private long mRefStart;
    private long mRefStop;
    private long REFRESH_DUR = 10000;
    double angle = 0;
    double angle1 = 0;
    int x0;
    int y0;
    int x;
    int y;
    RectF oval;

    private void drawRefreshing(Canvas canvas, float ration) {
        canvas.drawRect(0, 0, mWidth, PULL_HEIGHT, mBackPaint);

        mFrontPain.setStrokeWidth(5);
        oval = new RectF(mWidth / 2 - radius, PULL_HEIGHT / 2 - radius, mWidth / 2 + radius, PULL_HEIGHT / 2 + radius);
//        canvas.drawArc(oval, 0, 360, true, mFrontPain);
        canvas.drawCircle(mWidth/2,PULL_HEIGHT/2,radius,mFrontPain);

        x0 = mWidth / 2;
        y0 = PULL_HEIGHT / 2;

        mFrontPain.setStrokeWidth(3);

        angle = ration * Math.PI * 24;
        x = (int) (x0 + radius * Math.cos(angle) * 0.9);
        y = (int) (y0 + radius * Math.sin(angle) * 0.9);
        mPath.reset();
        mPath.moveTo(x0, y0);
        mPath.lineTo(x, y);
        canvas.drawPath(mPath, mFrontPain);

        angle1 = ration * 2 * Math.PI;
        x = (int) (x0 + radius * Math.cos(angle1) * 0.6);
        y = (int) (y0 + radius * Math.sin(angle1) * 0.6);
        mPath.reset();
        mPath.moveTo(x0, y0);
        mPath.lineTo(x, y);
        canvas.drawPath(mPath, mFrontPain);
    }



    private float getRefRation() {
        if (isRefresh) {
            return ((System.currentTimeMillis() - mRefStart) % REFRESH_DUR) / (float) REFRESH_DUR;
        } else {
            mStatus = AnimatorStatus.REL_DRAG;
            return 1;
        }

    }



    protected void startRefresh() {
        isRefresh = true;
        mStatus = AnimatorStatus.REFRESH;
        mRefStart = System.currentTimeMillis();
        mRefStop = mRefStart + REFRESH_DUR;
        requestLayout();
//        invalidate();
    }



    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}

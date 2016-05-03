package com.ybh.lovemeizi.widget.yrefreshview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by y on 2016/4/19.
 */
public class YRefreshLayout extends FrameLayout {
    private Context mContext;
    private float PULL_HEIGHT;
    private int HEADER_HEIGHT;
    private View childView;
    private boolean mIsRefreshing;
    private YHeaderView yHeaderView;
    private YFooterView yFooterView;
    private OnRefreshListener onRefreshListener;
    private float mTouchStartY;
    private float mCurrTouchY;
    private final static int PULL_DOWN_MODE = 1;
    private final static int PULL_UP_MODE = -1;
    private int pull_mode = PULL_DOWN_MODE;
    private ValueAnimator mGohomeAnimator;

    public YRefreshLayout(Context context) {
        this(context, null);
    }

    public YRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        PULL_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, mContext.getResources().getDisplayMetrics());
        HEADER_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, mContext.getResources().getDisplayMetrics());
        if (getChildCount() > 1) {
            throw new RuntimeException("only one child");
        }
        this.post(new Runnable() {
            @Override
            public void run() {
                childView = getChildAt(0);
                addHeader();
                addFooter();
                autoRefresh();
            }


        });

    }

    private void autoRefresh() {
        if (null != childView) {
            mIsRefreshing = true;
            childView.setTranslationY(HEADER_HEIGHT);
            yHeaderView.getLayoutParams().height = HEADER_HEIGHT;
            yHeaderView.requestLayout();
            yHeaderView.onStartRefresh();
            if (null != onRefreshListener) {
                onRefreshListener.onRefreshing();
            }
        }
    }

    /**
     * 刷新结束
     */
    public void finishRefreshing() {
        switch (pull_mode) {
            case PULL_DOWN_MODE:
                yHeaderView.setIsRefresh(false);
                break;
            case PULL_UP_MODE:
                yFooterView.setRefresh(false);
                break;

            default:
                break;
        }

        mIsRefreshing = false;
        goHomeAnimator();

    }

    private void addFooter() {
        yFooterView = new YFooterView(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.BOTTOM;
        yFooterView.setLayoutParams(layoutParams);
        addView(yFooterView);
    }

    private void addHeader() {
        yHeaderView = new YHeaderView(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.TOP;
        yHeaderView.setLayoutParams(layoutParams);
        addView(yHeaderView);
    }


    public interface OnRefreshListener {
        void onRefreshing();

        void onLoading();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsRefreshing) {//处于刷新状态,不再将事件传递给子view
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrTouchY = ev.getY();
                float y = mCurrTouchY - mTouchStartY;
                if (childView != null) {
                    //可以往下滚动,childView是第一个子view
                    if ((y > 0 && !ViewCompat.canScrollVertically(childView, -1))
                            //可以往上滚动
                            || (y < 0 && !ViewCompat.canScrollVertically(childView, 1))) {
                        return true; //自身拦截事件
                    }
                }
                return false; //事件继续往下传递给子view
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsRefreshing) { //处于刷新状态,本身不处理触摸事件
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                mIsRefreshing = false;
                float y = event.getY() - mTouchStartY;
                pull_mode = y > 0 ? PULL_DOWN_MODE : PULL_UP_MODE;
                //此处PULL_HEIGHT*2是为了保证当下拉或上拉的高度超过PULL_HEIGHT*2时,最大值为PULL_HEIGHT*2
                y = Math.min(PULL_HEIGHT * 2, Math.abs(y));
                //减速插值器,下拉或上拉时,速度先快后慢
                int refreshViewHeight = (int) (new DecelerateInterpolator(10).getInterpolation(y / PULL_HEIGHT / 2) * y / 2);
                float moveDistance = pull_mode == PULL_DOWN_MODE ? refreshViewHeight : -refreshViewHeight;
                if (childView != null) {
                    //下拉
                    if (pull_mode == PULL_DOWN_MODE && !isChildViewCanScrollDown()) {
                        childView.setTranslationY(moveDistance);
                        yHeaderView.isShowDown = true; //为了滑动的时候显示"下拉"
                        yHeaderView.getLayoutParams().height = refreshViewHeight;
                        yHeaderView.requestLayout();
                    } else if (pull_mode == PULL_UP_MODE && !isChildViewCanScrollUp()) {
                        childView.setTranslationY(moveDistance);
                        yFooterView.isShowUp=true;
                        yFooterView.getLayoutParams().height = refreshViewHeight;
                        yFooterView.requestLayout();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (childView != null) {
                    float height = Math.abs(childView.getTranslationY());
                    if (height > HEADER_HEIGHT) {
                        switch (pull_mode) {
                            case PULL_DOWN_MODE:
                                yHeaderView.isShowDown = false;
                                startRefresh(height);
                                break;
                            case PULL_UP_MODE:
                                yFooterView.isShowUp=false;
                                startLoad(height);
                                break;
                            default:
                                break;
                        }
                    } else {
                        goHomeAnimator();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 隐藏头/脚布局
     */
    private void goHomeAnimator() {
        mGohomeAnimator = ValueAnimator.ofFloat(childView.getTranslationY(), 0);
        mGohomeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                childView.setTranslationY(val);
                if (pull_mode == PULL_DOWN_MODE) {
                    yHeaderView.getLayoutParams().height = (int) val;
                    yHeaderView.requestLayout();
                } else if (pull_mode == PULL_UP_MODE) {
                    yFooterView.getLayoutParams().height = (int) -val;
                    yFooterView.requestLayout();
                }
            }
        });
        mGohomeAnimator.setDuration(500);
        mGohomeAnimator.start();
    }

    /**
     * 开始加载更多
     *
     * @param height
     */
    private void startLoad(float height) {
        mIsRefreshing = true;
        height = -height;
        mGohomeAnimator = ValueAnimator.ofFloat(height, -HEADER_HEIGHT);
        mGohomeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                childView.setTranslationY(val);
                yFooterView.getLayoutParams().height = (int) -val;
                yFooterView.requestLayout();
                if (val == -HEADER_HEIGHT) {
                    yFooterView.setStartRefresh();
                    if (onRefreshListener != null) {
                        onRefreshListener.onLoading();
                    }
                }
            }
        });
        mGohomeAnimator.setDuration(500);
        mGohomeAnimator.start();
    }

    /**
     * 开始刷新
     *
     * @param height
     */
    private void startRefresh(float height) {
        mIsRefreshing = true;
        //此时是下拉的高度大于头部高度
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(height, HEADER_HEIGHT);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                childView.setTranslationY(val);
                yHeaderView.getLayoutParams().height = (int) val;
                yHeaderView.requestLayout();
                if (val == HEADER_HEIGHT) { //高度相同时,进行刷新
                    yHeaderView.onStartRefresh();
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefreshing();
                    }
                }
            }
        });
        valueAnimator.setDuration(500);//属性动画执行时间
        valueAnimator.start();
    }

    private boolean isChildViewCanScrollDown() {
        return childView != null && ViewCompat.canScrollVertically(childView, -1);
    }

    //上滑
    private boolean isChildViewCanScrollUp() {
        return childView != null && ViewCompat.canScrollVertically(childView, 1);
    }

}

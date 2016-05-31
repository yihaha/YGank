package com.ybh.lovemeizi.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by y on 2016/5/25.
 */
public class YRollViewpager extends ViewPager {


    private MyPageAdapter myPageAdapter;
    public int mPreviousPoint; //上一个点的位置
    private ArrayList<String> imgsUrl;
    private Context mContext;
    //默认在0页
    private int mCurrentItem; //默认在0页
    private OnpageItemClickListener onpageItemClickListener;
    private long mLimitTime = 3000; //默认3秒
    private boolean isGiveupEvent; //在第一个/最后一个滑动时是否放弃触摸事件
    private int pos; //当前是哪个


    public YRollViewpager(Context context) {
        this(context, null);

    }

    public YRollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * 初始化数据
     *
     * @param imgs
     */
    public void initData(ArrayList<String> imgs) {
        this.imgsUrl = imgs;
        mCurrentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imgs.size();
        myPageAdapter = new MyPageAdapter();
        this.setAdapter(myPageAdapter);
        this.setCurrentItem(mCurrentItem);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            YRollViewpager.this.setCurrentItem(YRollViewpager.this.getCurrentItem() + 1);
            startRollPage();
        }
    };

    /**
     * 开始轮播
     */
    public void startRollPage() {
        /** 先移除消息,保证最多只有一个消息 */
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, mLimitTime);
    }

    public void stopRollPage() {
        mHandler.removeMessages(0);
    }

    public class MyPageAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 添加内容
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            pos = position % imgsUrl.size();
//            Log.w("mPreviousPoint", mPreviousPoint + "");
//            Log.w("instantiateItem", pos + "");
            ImageView imageView = new ImageView(mContext);

            String url = imgsUrl.get(pos);

            Glide.with(mContext)
                    .load(url)
                    .dontAnimate()
                    .centerCrop()
                    .into(imageView);

            imageView.setOnTouchListener(new OnTouchListener() {
                float downX;
                long downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //移出任务,不再轮播
                            mHandler.removeCallbacksAndMessages(null);
                            downX = event.getX();
                            downTime = System.currentTimeMillis();
                            break;


                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            float x = event.getX();
                            if (x == downX && System.currentTimeMillis() - downTime < 500) {
                                if (onpageItemClickListener != null) {
                                    onpageItemClickListener.clickPage(mPreviousPoint);
                                }
                            }

                            startRollPage();
                            break;

                    }


                    return true;
                }
            });

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //    从界面移出的时候会调用方法
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //移除所有的任务
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        switch (visibility) {
            case VISIBLE:
                startRollPage();

                break;

            case INVISIBLE:
            case GONE:
                stopRollPage();

                break;
        }
    }

    public interface OnpageItemClickListener {
        void clickPage(int position);
    }

    /**
     * 添加点击事件
     *
     * @param onpageItemClickListener
     */
    public void setOnpageItemClickListener(OnpageItemClickListener onpageItemClickListener) {
        this.onpageItemClickListener = onpageItemClickListener;
    }

//    float downX ;

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = ev.getX();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                float moveX = ev.getX();
//                if (mPreviousPoint == 0 && moveX > downX) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else if ((mPreviousPoint == imgs.length - 1) && (moveX < downX)) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//
//                break;
//        }
////
////
//        return super.onInterceptTouchEvent(ev);
//    }


    /**
     * 设置轮播间隔时间
     *
     * @param time
     */
    public void setmLimitTime(long time) {
        this.mLimitTime = time;
    }

    public void setSEGiveupEnvent(boolean seGiveupEnvent) {
        isGiveupEvent = seGiveupEnvent;
    }


}

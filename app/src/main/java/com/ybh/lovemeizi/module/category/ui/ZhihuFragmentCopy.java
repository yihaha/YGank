package com.ybh.lovemeizi.module.category.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.YApp;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.http.zhihu.KanZhihuApi;
import com.ybh.lovemeizi.model.gankio.FewDayData;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuAll;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuBean;
import com.ybh.lovemeizi.module.BaseFragment;
import com.ybh.lovemeizi.module.category.adapter.ZhihuAdapterCopy;
import com.ybh.lovemeizi.module.home.ui.DetailActivity;
import com.ybh.lovemeizi.utils.ACache;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.widget.YHFRecycleViewAdapter;
import com.ybh.lovemeizi.widget.YRollViewpager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by y on 2016/5/10.
 */
public class ZhihuFragmentCopy extends BaseFragment {
    private static final String TAG = "ZhihuFragment";

    private int currPage = 1;

    private String flag;
    private KanZhihuApi kanZhihuApi = ApiServiceFactory.getZhihuSingle();
    private long dateLimit = 24 * 60 * 60 * 1000; //一天的时间
    private List<KanzhihuBean> mZhihuList;
    private boolean isChange; //请求某一天的数据可能不存在,不存在时就将这个参数设置为true,在下拉刷新时,不再将currPage=1;
    private YHFRecycleViewAdapter<ZhihuAdapterCopy> mYHFRecycleViewAdapter;
    private List<FewDayData.YData> yGDatas;

    @Override
    protected int setContentLayout() {
        return R.layout.yfragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mZhihuList = new ArrayList<>();
        flag = getArguments().getString("flag");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        ZhihuAdapterCopy mAdapter = new ZhihuAdapterCopy(mZhihuList);
        mYHFRecycleViewAdapter = new YHFRecycleViewAdapter<>(mAdapter);
        mYHFRecycleViewAdapter.setConutSpan(mRecycleView);
        mRecycleView.setAdapter(mYHFRecycleViewAdapter);
        hfAdapterSet();


        // 刷新时，指示器旋转后变化的颜色
        mSwiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
//                if (!isChange) {
                currPage = 1;
//                }
                loadData(currPage);
            }
        });

        loadData(currPage);

        mRecycleViewScrollSet();
    }

    private boolean mIsLoading;//是否正出去加载中,包括下拉刷新,上拉加载

    /**
     * RecycleView滑动监听,主要处理上拉加载更多
     */
    private void mRecycleViewScrollSet() {
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //1代表从下滚上来
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    //没有在加载,也不是第一次进入才执行
                    if (!mIsLoading) {
                        mIsLoading = true;
                        addLoadingView();
                        loadData(++currPage);
                    }

                }

            }
        });
    }

    private int mPreviousPoint; //上一个点的位置

    /**
     * 添加轮播条所做的工作
     */
    private void hfAdapterSet() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.zhihu_header_layout, null);
        final YRollViewpager yRollViewpager = (YRollViewpager) layout.findViewById(R.id.viewpager);
        final TextView title = (TextView) layout.findViewById(R.id.title);
        final LinearLayout mLL = (LinearLayout) layout.findViewById(R.id.point_ll);
        mYHFRecycleViewAdapter.addHeaderView(layout);

        ACache aCache = ACache.get(YApp.yContext);
        FewDayData.YData data0 = (FewDayData.YData) aCache.getAsObject("mainData0");
        FewDayData.YData data1 = (FewDayData.YData) aCache.getAsObject("mainData1");
        FewDayData.YData data2 = (FewDayData.YData) aCache.getAsObject("mainData2");
        FewDayData.YData data3 = (FewDayData.YData) aCache.getAsObject("mainData3");

        yGDatas = new ArrayList<>();
        yGDatas.add(data0);
        yGDatas.add(data1);
        yGDatas.add(data2);
        yGDatas.add(data3);

        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < yGDatas.size(); i++) {
            urls.add(yGDatas.get(i).imgUrl);
        }
        yRollViewpager.initData(urls);
        title.setText(yGDatas.get(0).desc); //设置默认标题
        //初始化点
        for (int i = 0; i < urls.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.viewpager_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i != 0) {
                layoutParams.leftMargin = 9;
                imageView.setEnabled(false);
            }
            imageView.setLayoutParams(layoutParams);
            mLL.addView(imageView);

        }
        yRollViewpager.startRollPage();


        yRollViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //防止下拉刷新,与滑动冲突
                if (positionOffsetPixels > 0) {
                    mSwiRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onPageSelected(int position) {
                int pos = position % yGDatas.size();
                //改变点的颜色
                yRollViewpager.mPreviousPoint = pos;
                mLL.getChildAt(pos).setEnabled(true);
                mLL.getChildAt(mPreviousPoint).setEnabled(false);

                title.setText(yGDatas.get(pos).desc);
                mPreviousPoint = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        mSwiRefreshLayout.setEnabled(true);
                        break;
                }
            }
        });

        yRollViewpager.setOnpageItemClickListener(new YRollViewpager.OnpageItemClickListener() {
            @Override
            public void clickPage(int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ydata", yGDatas.get(position));
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });


    }

    private void loadData(final int page, String... type) {
        //通过计算得到今天,昨天等的数据
//        long time = new Date().getTime() - (page - 1) * dateLimit;
        /**在0~1点经过测试获取不到数据(可能只是恰好赶上了-.-),可能是后台数据没更新,为了尽量保证有数据
         * ,而不去处理没数据(稍微麻烦,如果是在公司开发中可根据实际情况来处理)的情况
         * ,这里多延迟10个小时
         * */
        long time = new Date().getTime() - (page - 1) * dateLimit - 10 * 60 * 60 * 1000;
        Date date = new Date(time);
//        昨日最新（yesterday）、近日热门（recent）和历史精华（archive）
        if ((type == null) || (!(type.length > 0))) {
            type = new String[1];
            type[0] = "yesterday";
        }
        kanZhihuApi.getZhihuJson(DateUtil.onDate2String(date, "yyyyMMdd"), type[0])
                .map(new Func1<KanzhihuAll, List<KanzhihuBean>>() {
                    @Override
                    public List<KanzhihuBean> call(KanzhihuAll kanzhihuAll) {
                        return kanzhihuAll.zhuhuAllList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<KanzhihuBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        setRefresh(false);
                        KLog.w(TAG, e.toString());
//                        ToastSnackUtil.snackbarLong(mRecycleView, TAG + "异常: " + e.toString());
                        mYHFRecycleViewAdapter.removeFooter();
                        mIsLoading = false;
                    }

                    @Override
                    public void onNext(final List<KanzhihuBean> kanzhihuBeen) {

                        KLog.w("zhifragment_next", "得到");
                        setRefresh(false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //将旧数据清除
                                if (page == 1 && mZhihuList.size() > 0) {
                                    mZhihuList.clear();
                                }
                                mZhihuList.addAll(kanzhihuBeen);
                                mYHFRecycleViewAdapter.getAdapter().setList(mZhihuList);
                                mYHFRecycleViewAdapter.removeFooter();
                                mIsLoading = false;
                            }
                        }, 1500);

                    }
                });
    }

    /**
     * 添加正在加载布局
     */
    private void addLoadingView() {

        /**
         * 注意,这里之所以不用View loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.bottomloading,null);
         * 是因为这样加载中的布局不会居中显示,需要parent,xml里的属性才起作用
         */
        View loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.bottomloading, mRecycleView, false);
        mYHFRecycleViewAdapter.addFooterView(loadingView);
    }

}

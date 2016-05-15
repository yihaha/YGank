package com.ybh.lovemeizi.module.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.socks.library.KLog;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.gankio.GankRetrofitService;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.model.gankio.FewDayData;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.model.gankio.TodayDataBean;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.module.home.adapter.DetailAdapter;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.utils.ShareUtil;
import com.ybh.lovemeizi.utils.SlidrUtil;
import com.ybh.lovemeizi.utils.ToastSnackUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DetailActivity extends BaseActivity {

//    @Bind(R.id.swipelayout)
//    SwipeRefreshLayout mSwiRefreshLayout;

    @Bind(R.id.detail_recycleview)
    RecyclerView mRecycleView;


    @Bind(R.id.collbarlayout)
    CollapsingToolbarLayout mCollBarLayout;

//    @Bind(R.id.appbarlayout1)
//    AppBarLayout mAppbarlayout1;

    @Bind(R.id.video_bg_img)
    ImageView mVideoImg;
//    private GankRetrofitService gankService;
    private List<GankData> mGankDatas;
    private DetailAdapter detailAdapter;
    private FewDayData.YData yData;

    @Override
    public int getContentViewId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        // 默认开启侧滑，默认是整个页码侧滑
        SlidrUtil.initSlidrDefaultConfig(this, true);
        yData = (FewDayData.YData) getIntent().getSerializableExtra("ydata");
        mGankDatas = yData.gankDataList;
        detailAdapter = new DetailAdapter(mGankDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(detailAdapter);
//        mSwiRefreshLayout.setColorSchemeResources(R.color.md_black_1000);
//        mSwiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//            }
//        });
//        mAppbarlayout1.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                KLog.w("verticalOffset**"+verticalOffset);
//                if (verticalOffset>=0){
//                    mSwiRefreshLayout.setEnabled(true);
//                }else {
//                    mSwiRefreshLayout.setEnabled(false);
//                }
//            }
//        });

    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setRefresh(true);
//            }
//        },300);
//    }

    @Override
    public void initData() {
//        gankService = ApiServiceFactory.getSingleService();
        yData = (FewDayData.YData) getIntent().getSerializableExtra("ydata");

        //设置标题
        setActivityTitle(DateUtil.onDate2String(yData.publishedAt), true);

        mCollBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGankDatas.size() > 0) {
                    GankData gankData = mGankDatas.get(mGankDatas.size()-1);
                    Intent webIntent = WebActivity.newIntent(DetailActivity.this, gankData.desc, gankData.type, gankData.url);
                    startActivity(webIntent);
                } else {
                    ToastSnackUtil.snackbarShort(mCollBarLayout, "请数据加载完成再试");
                }
            }
        });
    }

//    private void loadData(String year, String month, String day) {
//        Subscription subscribe = gankService.getTodayData(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day))
//                .map(new Func1<TodayDataBean, List<GankData>>() {
//                    //将数据进行转换,得到各个分类的数据,添加到同一个集合中
//                    @Override
//                    public List<GankData> call(TodayDataBean todayDataBean) {
//                        //清除就数据
//                        if (mGankDatas.size()>0){
//                            mGankDatas.clear();
//                        }
//                        return getDataList(todayDataBean.results);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<GankData>>() {
//                    @Override
//                    public void onCompleted() {
//                        KLog.w("onCompleted", "完成");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        setRefresh(false);
//                        KLog.w("onError", e + "");
//                    }
//
//                    @Override
//                    public void onNext(List<GankData> gankDatas) {
////                        mGankDatas.addAll(gankDatas);
//                        setRefresh(false);
//                        detailAdapter.notifyDataSetChanged();
//                    }
//                });
//        addSubscription(subscribe);
//    }

//    //将当天数据提取出来
//    private List<GankData> getDataList(TodayDataBean.Results results) {
////        ArrayList<GankData> currDatas = new ArrayList<>();
//        if (results.videoList != null) mGankDatas.addAll(results.videoList);
//        if (results.androidList != null) mGankDatas.addAll(results.androidList);
//        if (results.iosList != null) mGankDatas.addAll(results.iosList);
//        if (results.frontList != null) mGankDatas.addAll(results.frontList);
//        if (results.expandList != null) mGankDatas.addAll(results.expandList);
//        if (results.appList != null) mGankDatas.addAll(results.appList);
//        if (results.introsList != null) mGankDatas.addAll(results.introsList);
//
//        return mGankDatas;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_share:
                showShare();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShare() {
        if (mGankDatas.size() > 0) {
            GankData gankData = mGankDatas.get(0);
            ShareUtil.sdkShare(DetailActivity.this, gankData.url
                    , gankData.type, gankData.desc, Platform.SHARE_WEBPAGE);
        } else {
            ToastSnackUtil.snackbarShort(mCollBarLayout, "请数据加载完成再试");
        }
    }

//    /**
//     * 刷新
//     * @param tag
//     */
//    public void setRefresh(boolean tag){
//        if (mSwiRefreshLayout!=null){
//            if (!tag){
//                //刷新消失不会太快
//                mSwiRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mSwiRefreshLayout!=null) { //不做判null,可能会崩溃
//                            mSwiRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                },1500);
//            }else {
//                mSwiRefreshLayout.setRefreshing(true);
//            }
//        }
//    }

}

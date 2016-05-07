package com.ybh.lovemeizi.module.home.ui;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.GankRetrofitService;
import com.ybh.lovemeizi.http.GankServiceFactory;
import com.ybh.lovemeizi.model.GankData;
import com.ybh.lovemeizi.model.TodayDataBean;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.module.home.adapter.DetailAdapter;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.utils.ShareUtil;
import com.ybh.lovemeizi.utils.SlidrUtil;
import com.ybh.lovemeizi.utils.ToastSnackUtil;

import java.text.DateFormat;
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

    @Bind(R.id.detail_recycleview)
    RecyclerView mRecycleView;


    @Bind(R.id.collbarlayout)
    CollapsingToolbarLayout mCollBarLayout;

    @Bind(R.id.video_bg_img)
    ImageView mVideoImg;
    private GankRetrofitService gankService;
    private List<GankData> mGankDatas;
    private DetailAdapter detailAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        // 默认开启侧滑，默认是整个页码侧滑
        SlidrUtil.initSlidrDefaultConfig(this, true);
        mGankDatas = new ArrayList<>();
        detailAdapter = new DetailAdapter(mGankDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(detailAdapter);
    }

    @Override
    public void initData() {
        gankService = GankServiceFactory.getSingleService();
        Intent intent = getIntent();
        String date = intent.getStringExtra(Contant.Y_DATE);
        Date time = new Date(Long.parseLong(date));
        //设置标题
        setActivityTitle(DateUtil.onDate2String(time, "yyyy/MM/dd"), true);
        loadData(DateUtil.onDate2String(time, "yyyy"), DateUtil.onDate2String(time, "MM"), DateUtil.onDate2String(time, "dd"));
        mCollBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGankDatas.size()>0){
                    GankData gankData = mGankDatas.get(0);
                    Intent webIntent = new Intent(DetailActivity.this, WebActivity.class);
//                    webIntent.putExtra("url",gankData.url);
//                    webIntent.putExtra("desc",gankData.desc);
                    webIntent.putExtra(Contant.Y_GANKDATA,gankData);
                    startActivity(webIntent);
                }
            }
        });
    }

    private void loadData(String year, String month, String day) {
        Subscription subscribe = gankService.getTodayData(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day))
                .map(new Func1<TodayDataBean, List<GankData>>() {
                    //将数据进行转换,得到各个分类的数据,添加到同一个集合中
                    @Override
                    public List<GankData> call(TodayDataBean todayDataBean) {
                        return getDataList(todayDataBean.results);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankData>>() {
                    @Override
                    public void onCompleted() {
                        KLog.w("onCompleted", "完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.w("onError", e + "");
                    }

                    @Override
                    public void onNext(List<GankData> gankDatas) {
//                        mGankDatas.addAll(gankDatas);
                        detailAdapter.notifyDataSetChanged();
                    }
                });
        addSubscription(subscribe);
    }

    //将当天数据提取出来
    private List<GankData> getDataList(TodayDataBean.Results results) {
//        ArrayList<GankData> currDatas = new ArrayList<>();
        if (results.videoList != null) mGankDatas.addAll(results.videoList);
        if (results.androidList != null) mGankDatas.addAll(results.androidList);
        if (results.iosList != null) mGankDatas.addAll(results.iosList);
        if (results.frontList != null) mGankDatas.addAll(results.frontList);
        if (results.expandList != null) mGankDatas.addAll(results.expandList);
        if (results.appList != null) mGankDatas.addAll(results.appList);
        if (results.introsList != null) mGankDatas.addAll(results.introsList);

        return mGankDatas;
    }

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
                    , gankData.type,gankData.desc, Platform.SHARE_VIDEO);
        } else {
            ToastSnackUtil.snackbarShort(mCollBarLayout, "请数据加载完成再试");
        }
    }

}

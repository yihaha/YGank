package com.ybh.lovemeizi.module.home.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;


import com.socks.library.KLog;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.GankRetrofitService;
import com.ybh.lovemeizi.http.GankServiceFactory;
import com.ybh.lovemeizi.model.gankio.AllData;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.utils.PreferenceUtil;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.module.home.adapter.MainRecyclAdapter;
import com.ybh.lovemeizi.widget.yrefreshview.YRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_drawerlayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

//    @Bind(R.id.appbarlayout)
//    AppBarLayout mAppBarLayout;


    @Bind(R.id.meizi_listview)
    RecyclerView mRecycleView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.main_navigationview)
    NavigationView mNavigationView;

    @Bind(R.id.relayout)
    YRefreshLayout mRefreshLayout;

    private MainRecyclAdapter mainRecyclAdapter;
    private final static int AVGCOUNT = 10; //每页数据
    private int page = 1;

    private List<GankData> meiziList = new ArrayList<>();
    private GankRetrofitService gService = GankServiceFactory.getSingleService();

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {

//        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        //填充侧边栏头部
        mNavigationView.inflateHeaderView(R.layout.main_drawer_header);
        //填充侧边栏菜单
        mNavigationView.inflateMenu(R.menu.menu_nav);

        onNavigationViewItemChecked(mNavigationView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRefreshLayout.setOnRefreshListener(new YRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                page = 1;
                loadData(page);
            }

            @Override
            public void onLoading() {
                page++;
                loadData(page);
            }
        });

        mainRecyclAdapter = new MainRecyclAdapter(MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setHasFixedSize(true);//item固定高度可以提高性能
        mRecycleView.setAdapter(mainRecyclAdapter);

        // 初始化BmobSDK
        Bmob.initialize(this, Contant.BOMB_APPID);

        //初始化建表操作,执行一次后,就应该注释掉
//        BmobUpdateAgent.initAppVersion(this);

        //取消只有在wifi情况下才更新
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        //默认在wifi下才更新
        BmobUpdateAgent.update(this);

    }


    /**
     * 侧边栏选项
     *
     * @param mNavigationView
     */
    private void onNavigationViewItemChecked(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
//                int itemId = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_category:
                        Toast.makeText(MainActivity.this, "分类", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this, "关于", Toast.LENGTH_SHORT).show();
                        break;

                }

                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }


    @Override
    public void initData() {
//        loadData();
    }

    /**
     * 请求数据
     */
    private void loadData(final int page) {
        Subscription subscribe = Observable.zip(gService.getMeiziList(AVGCOUNT, page), gService.getVideoList(AVGCOUNT, page)
                , new Func2<AllData, AllData, AllData>() {
                    @Override
                    public AllData call(AllData picAll, AllData videoAll) {
                        return onMerageDesc(picAll, videoAll);
                    }
                })
                .map(new Func1<AllData, List<GankData>>() {
                    @Override
                    public List<GankData> call(AllData allData) {
                        return allData.results;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        mRefreshLayout.finishRefreshing();
                        KLog.w("onError", e + "");
                        finishReorLoad();
                    }

                    @Override
                    public void onNext(List<GankData> gankDatas) {
                        if (page == 1 && meiziList.size() > 0) { //刷新的时候要将旧数据清空
                            meiziList.clear();
                        }
                        finishReorLoad();
                        meiziList.addAll(gankDatas);
                        mainRecyclAdapter.setRefresh(meiziList);
//                        mRefreshLayout.finishRefreshing();
                    }
                });
        addSubscription(subscribe);

    }

    /**
     * 为了保持刷新效果小于1秒时,也有1秒的效果
     */
    private void finishReorLoad() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.finishRefreshing();
            }
        }, 1000);
    }

    /**
     * 将视频内容的说明设置到图片说明
     *
     * @param picAll
     * @param videoAll
     * @return
     */
    private AllData onMerageDesc(AllData picAll, AllData videoAll) {
        int maxLength = picAll.results.size() > videoAll.results.size() ? picAll.results.size() : videoAll.results.size();
        for (int i = 0; i < maxLength; i++) {
            GankData picGank = picAll.results.get(i);
            GankData videoGank = videoAll.results.get(i);
            //可能发生当前图片和当前视频不是同一天内容,进入当天详情时"描述内容"与首页内容不同,会误认为发生数据请求错误
            if (DateUtil.onDate2String(picGank.publishedAt, "yyyy/MM/dd")
                    .equals(DateUtil.onDate2String(videoGank.publishedAt, "yyyy/MM/dd"))) {
                picGank.desc =videoGank.desc;
            }
        }
        return picAll;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {

            case R.id.action_day_night_mode:
                PreferenceUtil preferenceUtil = new PreferenceUtil(MainActivity.this);
                boolean isNightMode = preferenceUtil.getBoolean(Contant.DAY_NIGHT_MODE);
                if (isNightMode) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
//                KLog.w("夜间模式",isNight);
                isNightMode = !isNightMode;
                preferenceUtil.saveBoolean(Contant.DAY_NIGHT_MODE, isNightMode);
                recreate();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

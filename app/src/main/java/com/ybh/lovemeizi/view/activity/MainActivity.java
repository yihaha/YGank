package com.ybh.lovemeizi.view.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.GankRetrofitService;
import com.ybh.lovemeizi.http.GankServiceFactory;
import com.ybh.lovemeizi.model.AllData;
import com.ybh.lovemeizi.model.GankData;
import com.ybh.lovemeizi.view.adapter.MainRecyclAdapter;

import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_drawerlayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //    @Bind(R.id.tablayout)
//    TabLayout mTabLayout;
//
//    @Bind(R.id.viewpager)
//    ViewPager mViewPager;
    @Bind(R.id.meizi_listview)
    RecyclerView mRecycleView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.main_navigationview)
    NavigationView mNavigationView;
    private MainRecyclAdapter mainRecyclAdapter;

    private GankRetrofitService gService = GankServiceFactory.getSingleService();

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
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


        mainRecyclAdapter = new MainRecyclAdapter(MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setHasFixedSize(true);//item固定高度可以提高性能
        mRecycleView.setAdapter(mainRecyclAdapter);

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
        loadData();
    }

    /**
     * 请求数据
     */
    private void loadData() {
        Observable.zip(gService.getMeiziList(10, 1), gService.getVideoList(10, 1)
                , new Func2<AllData, AllData, AllData>() {
                    @Override
                    public AllData call(AllData picAll, AllData videoAll) {
                        return onMerageDesc(picAll,videoAll);
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

                    }

                    @Override
                    public void onNext(List<GankData> gankDatas) {
                        mainRecyclAdapter.setRefresh(gankDatas);
                    }
                });

    }

    /**
     * 将视频内容的说明设置到图片说明
     * @param picAll
     * @param videoAll
     * @return
     */
    private AllData onMerageDesc(AllData picAll,AllData videoAll){
        int maxLength = picAll.results.size() > videoAll.results.size() ? picAll.results.size() : videoAll.results.size();
        for (int i=0;i<maxLength;i++){
            GankData gankData = picAll.results.get(i);
            gankData.desc=videoAll.results.get(i).desc;
        }
        return picAll;
    }


}

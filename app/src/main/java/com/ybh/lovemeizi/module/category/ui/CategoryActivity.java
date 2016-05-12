package com.ybh.lovemeizi.module.category.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.http.zhihu.KanZhihuApi;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuAll;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuBean;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.module.BaseFragment;
import com.ybh.lovemeizi.module.category.adapter.YViewPagerAdapter;
import com.ybh.lovemeizi.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class CategoryActivity extends BaseActivity {

    @Bind(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.category_viewpage)
    ViewPager mViewPager;

    @Bind(R.id.main_navigationview)
    NavigationView mNavigationView;

    private ArrayList<BaseFragment> yFragments;
    private YViewPagerAdapter yViewPagerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_category;
    }


    @Override
    protected void setStatusBar() {
        int color = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawerlayout), color, 0);
    }

    @Override
    public void initView() {
        setActivityTitle("分类", true);
        mTabLayout.setVisibility(View.VISIBLE);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        //填充侧边栏头部
        mNavigationView.inflateHeaderView(R.layout.main_drawer_header);
        //填充侧边栏菜单
        mNavigationView.inflateMenu(R.menu.menu_nav);
        onNavigationViewItemChecked(mNavigationView, mDrawerLayout);
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.fragments_title);
        yFragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString("flag", titles[i]);
            BaseFragment baseFragment = null;
            if (i == 0) {
                baseFragment = new ZhihuFragment();
            } else {
                baseFragment = new CategoryFragment();
            }
            baseFragment.setArguments(bundle);
            yFragments.add(baseFragment);
        }

        yViewPagerAdapter = new YViewPagerAdapter(getSupportFragmentManager(), titles, yFragments);
        mViewPager.setAdapter(yViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(titles.length); //预加载几页
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }



}

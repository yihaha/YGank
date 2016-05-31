package com.ybh.lovemeizi.module.category.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.module.BaseFragment;
import com.ybh.lovemeizi.module.category.adapter.YViewPagerAdapter;
import com.ybh.lovemeizi.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.Bind;

public class CategoryActivity extends BaseActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.category_viewpage)
    ViewPager mViewPager;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    private ArrayList<BaseFragment> yFragments;
    private YViewPagerAdapter yViewPagerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_category;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), colorPrimary, 0);
    }

    @Override
    public void initView() {
        setActivityTitle("分类", true);
        mTabLayout.setVisibility(View.VISIBLE);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
//        //填充侧边栏头部
//        mNavigationView.inflateHeaderView(R.layout.main_drawer_header);
//        //填充侧边栏菜单
//        mNavigationView.inflateMenu(R.menu.menu_nav);
//        onNavigationViewItemChecked(mNavigationView, mDrawerLayout);
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
                baseFragment = new ZhihuFragmentCopy();
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

    @Override
    protected void onResume() {
        super.onResume();
        defaultCheckedItem=R.id.nav_category;
        onNavigationViewItemChecked();
    }
}

package com.ybh.lovemeizi;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ybh.lovemeizi.view.BaseActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_drawerlayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.main_navigationview)
    NavigationView mNavigationView;

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
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                        Toast.makeText(MainActivity.this, "home", 0).show();
                        break;
                    case R.id.nav_category:
                        Toast.makeText(MainActivity.this, "分类", 0).show();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(MainActivity.this, "设置", 0).show();
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this, "关于", 0).show();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

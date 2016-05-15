package com.ybh.lovemeizi.module;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.umeng.analytics.MobclickAgent;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.YApp;
import com.ybh.lovemeizi.module.category.ui.CategoryActivity;
import com.ybh.lovemeizi.module.home.ui.MainActivity;
import com.ybh.lovemeizi.utils.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public  class BaseActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar mToolbar;

//    @Bind(R.id.drawer_header_img)
    CircleImageView mHeaderView;

    private CompositeSubscription compositeSubscription;
    private Class mClass;
    protected int colorPrimary;
    protected int defaultCheckedItem; //默认选中的菜单
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
            ButterKnife.bind(this);
//            mHeaderView= (CircleImageView) findViewById(R.id.drawer_header_img);
//            if (mHeaderView!=null){
//                mHeaderView.setImageResource(R.mipmap.header);
//            }
        }
        initToolbar();
        initView();
//        if (mHasDrawLayout){
//            onNavigationViewItemChecked();
//        }
        initData();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        colorPrimary = getResources().getColor(R.color.colorPrimary);
        setStatusBar();
    }

    /**
     * 与状态栏有关的设置
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,colorPrimary,0);
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }


    public  void initView(){};

    public  void initData(){};

    public int getContentViewId() {
        return 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (0 != getContentViewId()) {
            ButterKnife.unbind(this);
        }
        if (null != compositeSubscription) {
            compositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscribe) {
        if (null == compositeSubscription) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscribe);
    }

    public void setActivityTitle(String title, boolean showLeftView) {
        setTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 侧边栏选项
     */
    protected void onNavigationViewItemChecked() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setCheckedItem(defaultCheckedItem);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                if (item.isChecked()){
                    return true;
                }
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_home:
//                        Toast.makeText(BaseActivity.this, "home", Toast.LENGTH_SHORT).show();
                        mClass = MainActivity.class;
                        break;
                    case R.id.nav_category:
//                        Toast.makeText(BaseActivity.this, "分类", Toast.LENGTH_SHORT).show();
                        mClass = CategoryActivity.class;
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(BaseActivity.this, "待开发", Toast.LENGTH_SHORT).show();
//                        intent=new Intent(YApp.yContext, MainActivity.class);
//                        mClass = TestActivity.class;
                        return false;
//                        break;
                    case R.id.nav_about:
//                        Toast.makeText(BaseActivity.this, "关于", Toast.LENGTH_SHORT).show();
//                        intent=new Intent(YApp.yContext, MainActivity.class);
                        mClass = AboutActivity.class;
                        break;
                    default:
                        break;

                }


//                mDrawerLayout.closeDrawers();
//                startActivity(intent); //有些卡顿
                if (mClass != null) {
                    mDrawerLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(YApp.yContext, mClass));
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                    }, 300);
                }
                return true;
            }
        });

        mNavigationView.post(new Runnable() {
            @Override
            public void run() {
                CircleImageView avaImg = (CircleImageView) BaseActivity.this.findViewById(R.id.drawer_header_img);
                if (avaImg!=null) {
                    avaImg.setImageResource(R.mipmap.header);
                }
            }
        });

    }


}

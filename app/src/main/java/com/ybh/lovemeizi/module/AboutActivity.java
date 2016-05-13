package com.ybh.lovemeizi.module;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ybh.lovemeizi.BuildConfig;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.module.home.ui.WebActivity;
import com.ybh.lovemeizi.utils.SlidrUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.collbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    //    @Bind(R.id.toolbar)
//    Toolbar mToolbar;
    @Bind(R.id.version_code)
    TextView mVersionCode_view;

    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 默认开启侧滑，默认是整个页码侧滑
        SlidrUtil.initSlidrDefaultConfig(this, true);
        setActivityTitle(getString(R.string.about_introduce),true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initData() {
//        mCollapsingToolbarLayout.setTitle(getString(R.string.about_introduce));
        mVersionCode_view.setText("版本: "+ BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.developer_view)
    void toWebView(View view){
        Intent yibhIntent = WebActivity.newIntent(this, "yibh的踪迹...", "yibh的踪迹...", Contant.ME_URL);
        startActivity(yibhIntent);
    }

    @Override
    protected void setStatusBar() {}
}

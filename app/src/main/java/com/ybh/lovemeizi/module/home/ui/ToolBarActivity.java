package com.ybh.lovemeizi.module.home.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.module.BaseActivity;

import butterknife.Bind;

public abstract class ToolBarActivity extends BaseActivity {

    @Bind(R.id.appbarlayout)
    AppBarLayout mAppbarLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        if (isShowBackView()) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
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
     * 设置标题
     * @param title
     */
    public void setActivityTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title.subSequence(0,title.length()));
        }
    }

    /**
     * 是否显示左上返回图标
     *
     * @return
     */
    protected boolean isShowBackView() {
        return false;
    }

}

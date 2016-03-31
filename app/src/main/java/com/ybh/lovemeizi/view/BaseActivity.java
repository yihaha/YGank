package com.ybh.lovemeizi.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ybh.lovemeizi.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
            ButterKnife.bind(this);
        }
        initView();
        initData();
    }


    public abstract void initView();
    public abstract void initData();

    public int getContentViewId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        if (0!=getContentViewId()){
            ButterKnife.unbind(this);
        }
        super.onDestroy();
    }
}

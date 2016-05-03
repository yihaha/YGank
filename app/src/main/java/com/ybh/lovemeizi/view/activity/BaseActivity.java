package com.ybh.lovemeizi.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeSubscription compositeSubscription;

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
        super.onDestroy();
        if (0 != getContentViewId()) {
            ButterKnife.unbind(this);
        }
        if (null != compositeSubscription) {
            compositeSubscription.unsubscribe();
        }
    }

    public void addaddSubscription(Subscription subscribe) {
        if (null == compositeSubscription) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscribe);
    }

}

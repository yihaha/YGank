package com.ybh.lovemeizi.module;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

    public void addSubscription(Subscription subscribe) {
        if (null == compositeSubscription) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscribe);
    }

}

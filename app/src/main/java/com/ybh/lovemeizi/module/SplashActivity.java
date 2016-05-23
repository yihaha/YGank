package com.ybh.lovemeizi.module;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ybh.lovemeizi.BuildConfig;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.http.gankio.GankRetrofitService;
import com.ybh.lovemeizi.model.gankio.AllData;
import com.ybh.lovemeizi.module.home.ui.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.bg_img)
    ImageView mBgImg;

    @Bind(R.id.ll_view)
    LinearLayout mLinearLayout;

    @Bind(R.id.app_name)
    TextView mApp_name;

    @Bind(R.id.app_version_id)
    TextView mApp_versionId;

    private GankRetrofitService gankService = ApiServiceFactory.getSingleService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void initData() {
        mApp_name.setText(R.string.app_name);
        mApp_versionId.setText(BuildConfig.VERSION_NAME);

        gankService.getMeiziList(1, 1)
                .map(new Func1<AllData, String>() {
                    @Override
                    public String call(AllData allData) {
                        String url = allData.results.get(0).url;
                        return url;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onNext(String s) {
                        Picasso.with(SplashActivity.this).load(s).into(mBgImg);
                        beginAnimation();
                    }
                });
    }


    private void beginAnimation() {
        //背景图片
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.15f, 1f, 1.15f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true); //保持结束状态
        mBgImg.startAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        mLinearLayout.startAnimation(alphaAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}

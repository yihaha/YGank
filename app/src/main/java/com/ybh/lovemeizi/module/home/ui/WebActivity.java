package com.ybh.lovemeizi.module.home.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.module.BaseActivity;

import butterknife.Bind;

public class WebActivity extends BaseActivity {

    @Bind(R.id.number_progress_bar)
    NumberProgressBar topProgressBar;

    @Bind(R.id.webview)
    WebView mWebView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        WebSettings mWebSetting = mWebView.getSettings();
        /*********打开界面,自适应屏幕************/
        mWebSetting.setUseWideViewPort(true);//可以任意比例缩放
        mWebSetting.setLoadWithOverviewMode(true); //缩放至屏幕大小

        /*********页面支持缩放************/
        mWebSetting.setJavaScriptEnabled(true); //支持JS
        mWebSetting.setBuiltInZoomControls(true);//设置支持缩放
        mWebSetting.setSupportZoom(true); //支持缩放

        mWebSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        mWebSetting.setAppCacheEnabled(true); //缓存

        /*********获取焦点************/
        mWebView.requestFocusFromTouch();

    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("desc");
        setActivityTitle(title,true);
        mWebView.setWebViewClient(new YWebClient());
        mWebView.setWebChromeClient(new YWebChromClient());
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //后退
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    class YWebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class YWebChromClient extends WebChromeClient{
        //进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            topProgressBar.setProgress(newProgress);
            if (newProgress==100){
                topProgressBar.setVisibility(View.GONE);
            }else {
                topProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

}

package com.ybh.lovemeizi.module.home.ui;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.utils.ETCUtil;
import com.ybh.lovemeizi.utils.ShareUtil;
import com.ybh.lovemeizi.utils.SlidrUtil;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;

public class WebActivity extends BaseActivity {

    @Bind(R.id.number_progress_bar)
    NumberProgressBar topProgressBar;

    @Bind(R.id.webview)
    WebView mWebView;
    private String mUrl;
    private String mTitle;
    private String mDesc;

    @Override
    public int getContentViewId() {
        return R.layout.activity_web;
    }

    public static Intent newIntent(Context context, String title, String desc, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Contant.SHARE_TITLE, title);
        intent.putExtra(Contant.SHARE_DESC, desc);
        intent.putExtra(Contant.SHARE_URL, url);
        return intent;
    }

    @Override
    public void initView() {
        // 默认开启侧滑，默认是整个页码侧滑
        SlidrUtil.initSlidrDefaultConfig(this, true);

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

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Contant.SHARE_URL);
        mTitle = intent.getStringExtra(Contant.SHARE_TITLE);
        mDesc = intent.getStringExtra(Contant.SHARE_DESC);
        setActivityTitle(mTitle, true);
        mWebView.setWebViewClient(new YWebClient());
        mWebView.setWebChromeClient(new YWebChromClient());
        mWebView.loadUrl(mUrl);
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

    class YWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class YWebChromClient extends WebChromeClient {
        //进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (topProgressBar!=null&&(newProgress>0&&newProgress<=100)) {
                topProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    topProgressBar.setVisibility(View.GONE);
                } else {
                    topProgressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_share:
                showShare();
                return true;
            case R.id.web_copy:
                copyContent();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareUtil.sdkShare(WebActivity.this, mUrl, mTitle, mDesc, Platform.SHARE_WEBPAGE);
    }

    /**
     * copy链接到剪切板
     */
    private void copyContent() {
        ETCUtil.copyToClipBoard(WebActivity.this, mUrl, "链接复制成功!");
    }

    @Override
    public void finish() {
        /**为了防止以下错误,参考:http://blog.csdn.net/dreamer0924/article/details/34082687
         * Activity has leaked window Android.widget.ZoomButtonsController that was originally added here android.view.WindowLeaked:
         */
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}

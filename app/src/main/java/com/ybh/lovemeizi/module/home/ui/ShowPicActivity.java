package com.ybh.lovemeizi.module.home.ui;


import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.utils.DialogUtil;
import com.ybh.lovemeizi.utils.ImgSaveUtil;
import com.ybh.lovemeizi.utils.PreferenceUtil;
import com.ybh.lovemeizi.utils.SlidrUtil;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.widget.YBottomSheetDialogView;
import com.ybh.lovemeizi.widget.YDialog;
import com.ybh.lovemeizi.widget.slidr.model.SlidrInterface;

import cn.sharesdk.framework.Platform.ShareParams;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowPicActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.show_meizi_img)
    ImageView mImg;

    @Bind(R.id.appbarlayout)
    AppBarLayout mAppbarLayout;


    public static final String MEIZI_IMG = "meiziImg";

    private String imgUrl;

    @Override
    public int getContentViewId() {
        return R.layout.activity_show_pic;
    }


    @Override
    public void initView() {
//        ViewCompat.setTransitionName(mImg,MEIZI_IMG);
        // 默认开启侧滑，默认是整个页码侧滑
        mSlidrInterface = SlidrUtil.initSlidrDefaultConfig(this, true);

    }

    /**
     * 控制滑动与否的接口
     */
    protected SlidrInterface mSlidrInterface;

    @Override
    public void initData() {
        imgUrl = getIntent().getStringExtra("imgUrl");
        String date = getIntent().getStringExtra("date");
        setActivityTitle(date, true);
//        Glide.with(ShowPicActivity.this).load(imgUrl).into(mImg);
        Picasso.with(this).load(imgUrl).into(mImg);

        mImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showShare();
                return true;
            }
        });
    }


    private void showShare() {
        ShareSDK.initSDK(this);
        PreferenceUtil preferenceUtil = new PreferenceUtil(ShowPicActivity.this);
        boolean isNightMode = preferenceUtil.getBoolean(Contant.DAY_NIGHT_MODE);
        final YBottomSheetDialogView shareDialog = new YBottomSheetDialogView(ShowPicActivity.this, isNightMode == true ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        shareDialog.bottomClickCallback(new YBottomSheetDialogView.BottomClickListener() {
            @Override
            public void cancleShare() {
                shareDialog.dismissDialog();
            }

            @Override
            public void onItemcListener(String item) {
                ShareParams sp = new ShareParams();
                sp.setTitle("分享图片");  //分享标题
                sp.setText("图片文本");   //分享文本
                sp.setImageUrl(imgUrl);//网络图片rul
                sp.setTitleUrl(imgUrl);  //网友点进链接后，可以看到分享的详情
                sp.setUrl(imgUrl);   //网友点进链接后，可以看到分享的详情
                sp.setShareType(Platform.SHARE_IMAGE);//非常重要：一定要设置分享属性
                switch (item) {
                    case "微信":
                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.SSOSetting(false);
                        wechat.setPlatformActionListener(ShowPicActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                        break;
                    case "朋友圈":

                        //3、非常重要：获取平台对象
                        Platform wechatMoment = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoment.setPlatformActionListener(ShowPicActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechatMoment.share(sp);
                        break;

                    case "QQ":

//                        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
//                        sp.setTitle("分享图片");  //分享标题
//                        sp.setText("图片文本");   //分享文本
//                        sp.setImageUrl(imgUrl);//网络图片rul
//                        sp.setUrl(myAppUrl);   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform qqchat = ShareSDK.getPlatform(QQ.NAME);
                        qqchat.setPlatformActionListener(ShowPicActivity.this); // 设置分享事件回调
                        // 执行分享
                        qqchat.share(sp);
                        break;

                    case "QQ空间":

                        //3、非常重要：获取平台对象
                        Platform qqZonechat = ShareSDK.getPlatform(QZone.NAME);
                        qqZonechat.setPlatformActionListener(ShowPicActivity.this); // 设置分享事件回调
                        // 执行分享
                        qqZonechat.share(sp);
                        break;
                    case "新浪微博":

                        //3、非常重要：获取平台对象
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(ShowPicActivity.this); // 设置分享事件回调
                        // 执行分享
                        sinaWeibo.share(sp);
                        break;

                    default:
                        break;

                }
                shareDialog.dismissDialog();
            }
        });

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String name = platform.getName();
//        Toast.makeText(ShowPicActivity.this, "分享成功" + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        String name = platform.getName();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        String name = platform.getName();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picacitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                showSaveImgDialog();
                break;
            case R.id.menu_share:
                showShare();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存图片的提示
     */
    private void showSaveImgDialog() {
        final YDialog yDialog = DialogUtil.getYDialog(ShowPicActivity.this, R.layout.ydialog_default);
        TextView dialogTitle = (TextView) yDialog.findViewById(R.id.ydialog_title);
        dialogTitle.setText("保存图片到本地?");
        yDialog.findViewById(R.id.ydialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscription subscribe = ImgSaveUtil.saveImg(ShowPicActivity.this, imgUrl, "ygank")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Uri>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(mImg, "异常: " + e.getMessage() + "\n请再次尝试", Snackbar.LENGTH_LONG).setAction("知道了", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {}
                                }).show();
                            }

                            @Override
                            public void onNext(Uri uri) {
                                File ygankDir = new File(Environment.getExternalStorageDirectory(), "ygank");
                                Snackbar.make(mImg, String.format("图片已保存到%s文件夹下", ygankDir.getAbsolutePath()), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                DialogUtil.dismiss();
                addSubscription(subscribe);
            }
        });
    }

}

package com.ybh.lovemeizi.view.activity;


import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.utils.PreferenceUtil;
import com.ybh.lovemeizi.utils.SlidrUtil;
import com.ybh.lovemeizi.widget.YBottomSheetDialogView;
import com.ybh.lovemeizi.widget.slidr.model.SlidrInterface;

import cn.sharesdk.framework.Platform.ShareParams;

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
import uk.co.senab.photoview.PhotoView;

public class ShowPicActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.meizi_img)
    PhotoView mImg;
    private String imgUrl;

    private String myAppUrl = "http://fir.im/7vh1";

    @Override
    public int getContentViewId() {
        return R.layout.activity_show_pic;
    }


    @Override
    public void initView() {
//        if (mEnableSlidr && !SpUtil.readBoolean("disableSlide")) {
//            // 默认开启侧滑，默认是整个页码侧滑
        mSlidrInterface = SlidrUtil
                .initSlidrDefaultConfig(this, true);
//        }

        mImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showShare();
                return true;
            }
        });

    }

    /**
     * 控制滑动与否的接口
     */
    protected SlidrInterface mSlidrInterface;

    @Override
    public void initData() {
        imgUrl = getIntent().getStringExtra("imgUrl");
        Glide.with(ShowPicActivity.this).load(imgUrl).into(mImg);
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
}

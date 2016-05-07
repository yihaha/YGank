package com.ybh.lovemeizi.utils;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.widget.YBottomSheetDialogView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by y on 2016/5/7.
 */
public class ShareUtil {

    /**
     * 利用mob.com的集成分享
     * @param context
     * @param url
     */
    public static void sdkShare(final Context context, final String url, final String shTitle, final String shContent, final int shType){
            ShareSDK.initSDK(context);
            PreferenceUtil preferenceUtil = new PreferenceUtil(context);
            boolean isNightMode = preferenceUtil.getBoolean(Contant.DAY_NIGHT_MODE);
            final YBottomSheetDialogView shareDialog = new YBottomSheetDialogView(context, isNightMode == true ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

            shareDialog.bottomClickCallback(new YBottomSheetDialogView.BottomClickListener() {
                @Override
                public void cancleShare() {
                    shareDialog.dismissDialog();
                }

                @Override
                public void onItemcListener(String item) {
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setTitle(shTitle);  //分享标题
                    sp.setText(shContent);   //分享文本
                    sp.setImageUrl(url);//网络图片rul
                    sp.setTitleUrl(url);  //网友点进链接后，可以看到分享的详情
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情
                    sp.setShareType(shType);//非常重要：一定要设置分享属性
                    switch (item) {
                        case "微信":
                            //3、非常重要：获取平台对象
                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                            wechat.SSOSetting(false);
//                            wechat.setPlatformActionListener(new ); // 设置分享事件回调
                            // 执行分享
                            wechat.share(sp);
                            break;
                        case "朋友圈":

                            //3、非常重要：获取平台对象
                            Platform wechatMoment = ShareSDK.getPlatform(WechatMoments.NAME);
//                            wechatMoment.setPlatformActionListener(); // 设置分享事件回调
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
//                            qqchat.setPlatformActionListener(); // 设置分享事件回调
                            // 执行分享
                            qqchat.share(sp);
                            break;

                        case "QQ空间":

                            //3、非常重要：获取平台对象
                            Platform qqZonechat = ShareSDK.getPlatform(QZone.NAME);
//                            qqZonechat.setPlatformActionListener(); // 设置分享事件回调
                            // 执行分享
                            qqZonechat.share(sp);
                            break;
                        case "新浪微博":

                            //3、非常重要：获取平台对象
                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                            sinaWeibo.setPlatformActionListener(); // 设置分享事件回调
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

}

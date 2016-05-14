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
     *
     * @param context
     * @param url       网址
     * @param shTitle
     * @param shContent
     * @param shType
     */
    public static void sdkShare(final Context context, final String url, final String shTitle, final String shContent, final int shType) {
        ShareSDK.initSDK(context);
        final YBottomSheetDialogView shareDialog = new YBottomSheetDialogView(context);

        shareDialog.bottomClickCallback(new YBottomSheetDialogView.BottomClickListener() {
            @Override
            public void cancleShare() {
                shareDialog.dismissDialog();
            }

            @Override
            public void onItemcListener(String item) {
                String testUrl = "http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg";
                Platform.ShareParams sp = new Platform.ShareParams();
                sp.setShareType(shType);                                              //非常重要：一定要设置分享属性
                switch (item) {
                    case "微信":
                        sp.setTitle(shTitle);
                        sp.setText(shContent);
                        if (shType == Platform.SHARE_WEBPAGE) {
                            sp.setImageUrl(testUrl);
                            sp.setUrl(url);
                        } else if (shType == Platform.SHARE_IMAGE) {
                            sp.setImageUrl(url);
                        }
                        //获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        // 执行分享
                        wechat.share(sp);
                        break;
                    case "朋友圈":
                        sp.setTitle(shTitle);
                        sp.setText(shContent);
//                        //当前项目中故意调的和上面不同,工具实际效果来
//                        sp.setTitle(shContent);
//                        sp.setText(shTitle); //显示详细内容
                        if (shType == Platform.SHARE_WEBPAGE) {
                            sp.setImageUrl(testUrl);
                            sp.setUrl(url);
                        } else if (shType == Platform.SHARE_IMAGE) {
                            sp.setImageUrl(url);
                        }
                        Platform wechatMoment = ShareSDK.getPlatform(WechatMoments.NAME);
                        // 执行分享
                        wechatMoment.share(sp);
                        break;

                    case "QQ":

                        if (shType == Platform.SHARE_WEBPAGE) {
                            sp.setTitle(shTitle);
                            sp.setTitleUrl(url);
                            sp.setText(shContent);
                            sp.setImageUrl(testUrl);
                        } else if (shType == Platform.SHARE_IMAGE) {
                            sp.setImageUrl(url);
                        }

                        Platform qqchat = ShareSDK.getPlatform(QQ.NAME);
                        // 执行分享
                        qqchat.share(sp);
                        break;

                    case "QQ空间":

                        sp.setTitle(shTitle);
                        sp.setTitleUrl(url);
                        sp.setText(shContent);
                        sp.setSite(shTitle);
                        sp.setSiteUrl(url);
                        if (shType == Platform.SHARE_WEBPAGE) {
                            sp.setImageUrl(testUrl);
                        } else if (shType == Platform.SHARE_IMAGE) {
                            sp.setImageUrl(url);
                        }

                        Platform qqZonechat = ShareSDK.getPlatform(QZone.NAME);
                        // 执行分享
                        qqZonechat.share(sp);
                        break;
                    case "新浪微博":
                        sp.setText(shContent);                                                  //分享文本
                        if (shType == Platform.SHARE_WEBPAGE) {
                            sp.setUrl(url);
                            sp.setImageUrl(testUrl);
                        } else if (shType == Platform.SHARE_IMAGE) {
                            sp.setText(shTitle);
                            sp.setImageUrl(url);
                        }
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

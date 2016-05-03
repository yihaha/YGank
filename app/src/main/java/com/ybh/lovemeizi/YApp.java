package com.ybh.lovemeizi;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.socks.library.KLog;
import com.ybh.lovemeizi.utils.PreferenceUtil;

/**
 * Created by y on 2016/4/7.
 */
public class YApp extends Application {
    public static Context yContext;

    @Override
    public void onCreate() {
        super.onCreate();
        yContext = this;

        PreferenceUtil preferenceUtil = new PreferenceUtil(yContext);
        boolean isNightMode = preferenceUtil.getBoolean(Contant.DAY_NIGHT_MODE);
        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            //设置该app的主题根据时间不同显示
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        KLog.init(BuildConfig.LOG_DEBUG);
    }
}

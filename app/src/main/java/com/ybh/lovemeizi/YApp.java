package com.ybh.lovemeizi;

import android.app.Application;
import android.content.Context;

/**
 * Created by y on 2016/4/7.
 */
public class YApp extends Application {
    public static Context yContext;
    @Override
    public void onCreate() {
        super.onCreate();
        yContext=this;
    }
}

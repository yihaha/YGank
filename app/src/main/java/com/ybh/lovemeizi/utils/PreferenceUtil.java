package com.ybh.lovemeizi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by y on 2016/4/20.
 */
public class PreferenceUtil {
    private final static String YSH_PRE = "YI_MEIZI";
    private static SharedPreferences defaultSharedPreferences;
    private Context mContext;
    private final SharedPreferences.Editor mEdit;

    public PreferenceUtil(Context context) {
        this.mContext = context;
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEdit = defaultSharedPreferences.edit();
    }

    public void saveBoolean(String key, boolean value) {
        mEdit.putBoolean(key, value).apply();
    }

    public void saveString(String key, String  value) {
        mEdit.putString(key, value).apply();
    }

    public boolean getBoolean(String key,boolean ...value){
        if (null!=value&&value.length>0) {
            return defaultSharedPreferences.getBoolean(key, value[0]);
        }else {
            return defaultSharedPreferences.getBoolean(key,false);
        }
    }

    public String getString(String key,String value){
        return defaultSharedPreferences.getString(key,value);
    }
}

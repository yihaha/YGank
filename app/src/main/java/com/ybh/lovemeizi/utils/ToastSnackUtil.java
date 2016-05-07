package com.ybh.lovemeizi.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ybh.lovemeizi.YApp;

/**
 * Created by y on 2016/5/7.
 */
public class ToastSnackUtil {

    public static void snackbarShort(View view,String content){
        Snackbar.make(view,content, Snackbar.LENGTH_SHORT).show();
    }


    public static void toastShort(String content){
        Toast.makeText(YApp.yContext,content,Toast.LENGTH_SHORT).show();
    }

}

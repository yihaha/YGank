package com.ybh.lovemeizi.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ybh.lovemeizi.widget.YDialog;

/**
 * Created by y on 2016/4/25.
 */
public class DialogUtil {

    public static YDialog getYDialog(Context context,int mLayout){
        View inflate = LayoutInflater.from(context).inflate(mLayout, null);
        YDialog yDialog = new YDialog(context, inflate);

        return yDialog;
    }

}

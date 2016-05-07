package com.ybh.lovemeizi.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.widget.YDialog;

/**
 * Created by y on 2016/4/25.
 */
public class DialogUtil {

    private static YDialog yDialog;

    public static YDialog getYDialog(Context context, int mLayout){
        View inflate = LayoutInflater.from(context).inflate(mLayout, null);
        yDialog = new YDialog(context, inflate);
        //点击取消,dialog消失,传进来的layout,取消都应该有R.id.ydialog_cancel,方便统一处理
        yDialog.findViewById(R.id.ydialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yDialog.dismiss();
            }
        });
        yDialog.show();
        return yDialog;
    }

    public static void dismiss(){
        if (yDialog!=null){
            yDialog.dismiss();
        }
    }

}

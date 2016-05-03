package com.ybh.lovemeizi.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ybh.lovemeizi.R;

/**
 * Created by y on 2016/4/24.
 */
public class YDialog extends Dialog {
//    private View mView;
    public YDialog(Context context,View view) {
        this(context, R.style.YDialogStylet);
//        this.mView=view;
        addContentView(view, new ViewGroup.LayoutParams(-2, -2));
    }

    public YDialog(Context context, int themeResId) {
        super(context, themeResId);
//        View diyDialog = View.inflate(context, R.layout.forward_dialog, null);
//        addContentView(mView, new ViewGroup.LayoutParams(-2, -2));
    }
}

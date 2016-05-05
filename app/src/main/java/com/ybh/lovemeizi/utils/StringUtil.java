package com.ybh.lovemeizi.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;

/**
 * Created by y on 2016/5/5.
 */
public class StringUtil {

    /**
     * 文字样式,比如斜体,加粗,颜色等
     * @param context
     * @param content
     * @param style
     * @return
     */
    public static SpannableString setStringStyle(Context context, String content, int style){
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new TextAppearanceSpan(context,style),0,content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}

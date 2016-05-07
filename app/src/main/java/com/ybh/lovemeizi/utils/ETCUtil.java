package com.ybh.lovemeizi.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by y on 2016/5/7.
 */
public class ETCUtil {

    /**
     * 将内容copy到剪切板
     * @param context
     * @param text
     * @param msg
     */
    public static void copyToClipBoard(Context context, String text, String msg) {
        ClipData clipData = ClipData.newPlainText("ygank_copy", text);
        ClipboardManager manager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        ToastSnackUtil.toastShort(msg);
    }

}

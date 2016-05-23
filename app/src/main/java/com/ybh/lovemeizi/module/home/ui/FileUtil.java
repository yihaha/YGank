package com.ybh.lovemeizi.module.home.ui;

import android.os.Environment;

import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.YApp;

import java.io.File;

/**
 * Created by y on 2016/5/23.
 */
public class FileUtil {

    /**
     * 得到缓存目录
     * @return
     */
    public static File getCacheFile(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //得到sdk卡的缓存目录
            return new File(YApp.yContext.getExternalCacheDir(), Contant.CACHE_DIR);
        }

        return new File(YApp.yContext.getCacheDir(), Contant.CACHE_DIR);
    }

}

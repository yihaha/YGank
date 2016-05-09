package com.ybh.lovemeizi.model.gankio;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 当天内容
 * Created by y on 2016/4/6.
 */
public class TodayDataBean extends BaseData {
    public List<String> category;
    public Results results;

    public class Results {
        @SerializedName("休息视频")
        public List<GankData> videoList;

        @SerializedName("Android")
        public List<GankData> androidList;

        @SerializedName("iOS")
        public List<GankData> iosList;

        @SerializedName("前端")
        public List<GankData> frontList;

        @SerializedName("拓展资源")
        public List<GankData> expandList;

        @SerializedName("App")
        public List<GankData> appList;

        @SerializedName("瞎推荐")
        public List<GankData> introsList;

        @SerializedName("福利")
        public List<GankData> meiziList;
    }
}

package com.ybh.lovemeizi.model.gankio;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by y on 2016/5/15.
 */
public class FewDayData extends BaseData {
    @SerializedName("results")
    public List<YData> results;

    //某一天的数据
    public class YData implements Serializable{
        @SerializedName("_id")
        public String id;
        @SerializedName("content")
        public String content;
        @SerializedName("publishedAt")
        public Date publishedAt;
        @SerializedName("title")
        public String title;

        //下面是自己加入的数据,为了首页和进入详情页做准备
        public String imgUrl; //首页展示图片
        //展示在首页的描述信息,取视频描述,如果没有视频,就去android类型的第一个描述
        public String desc;
        //从content中解析出来的数据
        public List<GankData> gankDataList;
    }

}

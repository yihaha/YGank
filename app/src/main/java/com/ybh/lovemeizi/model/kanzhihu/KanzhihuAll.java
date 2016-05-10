package com.ybh.lovemeizi.model.kanzhihu;

import com.google.gson.annotations.SerializedName;
import com.ybh.lovemeizi.model.gankio.BaseData;

import java.util.List;

/**
 * Created by y on 2016/5/10.
 */
public class KanzhihuAll {
    /**
     * {
     * "error":"",
     * "count":32,
     * "answers":[
     * {
     * }]
     * }
     */

    @SerializedName("answers")
    public List<KanzhihuBean> zhuhuAllList; //问题(包含答案的id)列表
    @SerializedName("count")
    public int ansersCount;
    public String error;

}

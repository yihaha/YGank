package com.ybh.lovemeizi.model.gankio;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 所有数据
 * Created by y on 2016/4/6.
 */
public class AllData extends BaseData {
    @SerializedName("results")
    public List<GankData> results;
}

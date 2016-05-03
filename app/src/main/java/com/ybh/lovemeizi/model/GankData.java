package com.ybh.lovemeizi.model;

import java.util.Date;

/**
 * 每个具体数据对应的属性(基本相同)
 * Created by y on 2016/4/6.
 */
public class GankData {
    //数据都有相同的属性
    /**
     * _id : 56cc6d23421aa95caa707bdf
     * _ns : ganhuo
     * createdAt : 2015-08-07T01:36:06.932Z
     * desc : Display GitHub code in tree format
     * publishedAt : 2015-08-07T03:57:48.81Z
     * type : 拓展资源
     * url : https://github.com/buunguyen/octotree
     * used : true
     * who : lxxself
     */
    public String id;
    public String ns;
    public Date createdAt;
    public String desc;
    public Date publishedAt;
    public String type;
    public String url;
    public boolean used;
    public String who;

}

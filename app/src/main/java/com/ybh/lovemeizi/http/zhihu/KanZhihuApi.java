package com.ybh.lovemeizi.http.zhihu;

import com.ybh.lovemeizi.model.kanzhihu.KanzhihuAll;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by y on 2016/5/9.
 */
public interface KanZhihuApi {


    /**获取内容列表 getpostanswers/20160509/archive
     * ydate:20160508
     * ytyep:昨日最新（yesterday）、近日热门（recent）和历史精华（archive）
     * */
    @GET("getpostanswers/{ydate}/{ytype}")
    Observable<KanzhihuAll> getZhihuJson(@Path("ydate") String yDate,@Path("ytype") String yType);


}

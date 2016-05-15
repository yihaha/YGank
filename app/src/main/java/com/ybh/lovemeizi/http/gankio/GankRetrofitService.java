package com.ybh.lovemeizi.http.gankio;

import com.ybh.lovemeizi.model.gankio.AllData;
import com.ybh.lovemeizi.model.gankio.FewDayData;
import com.ybh.lovemeizi.model.gankio.TodayDataBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by y on 2016/4/6.
 */
public interface GankRetrofitService {

    //当天数据
    @GET("day/{year}/{month}/{day}")
    Observable<TodayDataBean> getTodayData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

//        @GET("day/{year}/{month}/{day}")
//        Call<TodayDataBean> getDayData(@Path("year") int year,@Path("month") int month,@Path("day") int day );

    //图片列表
    @GET("data/福利/{pageCount}/{currPage}")
    Observable<AllData> getMeiziList(@Path("pageCount") int pageCount, @Path("currPage") int currPage);

    //视频列表
    @GET("data/休息视频/{pageCount}/{currPage}")
    Observable<AllData> getVideoList(@Path("pageCount") int pageCount, @Path("currPage") int currPage);

    //所有数据列表
//        http://gank.io/api/data/all/20/2
    @GET("data/all/{pageCount}/{currPage}")
    Observable<AllData> getAllDataList(@Path("pageCount") int pageCount, @Path("currPage") int currPage);

    /**
     * http://gank.io/api/data/Android/10/1
     * http://gank.io/api/data/福利/10/1
     * http://gank.io/api/data/iOS/20/2
     */

    //android 信息列表
    @GET("data/Android/{pageCount}/{currPage}")
    Observable<AllData> getAndroidList(@Path("pageCount") int pageCount, @Path("currPage") int currPage);

    //ios信息列表
    @GET("data/iOS/{pageCount}/{currPage}")
    Observable<AllData> getIosList(@Path("pageCount") int pageCount, @Path("currPage") int currPage);

    //获取指定天数的数据 http://gank.io/api/history/content/2/1
    @GET("history/content/{pageCount}/{currPage}")
    Observable<FewDayData> getFewDayData(@Path("pageCount") int pageCount,@Path("currPage") int currPage);

}

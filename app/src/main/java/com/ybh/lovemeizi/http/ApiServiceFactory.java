package com.ybh.lovemeizi.http;

import com.ybh.lovemeizi.http.gankio.GankRetrofit;
import com.ybh.lovemeizi.http.gankio.GankRetrofitService;
import com.ybh.lovemeizi.http.zhihu.KanZhihuApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by y on 2016/4/12.
 */
public class ApiServiceFactory {
    private final static String ZHIHU_RUL = "http://api.kanzhihu.com/";
    static final Object obj = new Object();
    private static GankRetrofitService service;
    private static KanZhihuApi zhuhuApi;

    public static GankRetrofitService getSingleService() {
        synchronized (obj) {
            if (null == service) {
                service = new GankRetrofit().getService();
            }
        }
        return service;
    }

    public static KanZhihuApi getZhihuSingle() {
        synchronized (obj) {
            if (null == zhuhuApi) {
                zhuhuApi = new Retrofit.Builder()
                        .baseUrl(ZHIHU_RUL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build().create(KanZhihuApi.class);
            }
        }
        return zhuhuApi;
    }
}

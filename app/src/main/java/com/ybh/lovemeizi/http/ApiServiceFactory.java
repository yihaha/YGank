package com.ybh.lovemeizi.http;

import com.ybh.lovemeizi.http.gankio.GankRetrofitService;
import com.ybh.lovemeizi.http.zhihu.KanZhihuApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by y on 2016/4/12.
 */
public class ApiServiceFactory {
    private final static String GANK_URL = "http://gank.io/api/";
    private final static String ZHIHU_RUL = "http://api.kanzhihu.com/";
    static final Object obj = new Object();
    private static GankRetrofitService service;
    private static KanZhihuApi zhuhuApi;

    /**
     * 参考:https://drakeet.me/retrofit-2-0-okhttp-3-0-config
     * 主要想设置超时时间
     *
     * @return
     */
    private static OkHttpClient getClient() {
        OkHttpClient okHttpClient
                = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //出现错误进行重新连接
                .connectTimeout(15, TimeUnit.SECONDS) //超时时间
                .build();
        return okHttpClient;
    }

    public static GankRetrofitService getSingleService() {

        synchronized (obj) {
            if (null == service) {
                Retrofit retrofit = new Retrofit.Builder()
                        .client(getClient())
                        .baseUrl(GANK_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                service = retrofit.create(GankRetrofitService.class);
            }
        }
        return service;
    }

    public static KanZhihuApi getZhihuSingle() {
        synchronized (obj) {
            if (null == zhuhuApi) {
                zhuhuApi = new Retrofit.Builder()
                        .client(getClient())
                        .baseUrl(ZHIHU_RUL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build().create(KanZhihuApi.class);
            }
        }
        return zhuhuApi;
    }
}

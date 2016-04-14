package com.ybh.lovemeizi.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by y on 2016/4/6.
 */
public class GankRetrofit {
    private final String GANK_URL="http://gank.io/api/" ;
    private final GankRetrofitService gankRetrofitService;

    GankRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GANK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        gankRetrofitService = retrofit.create(GankRetrofitService.class);
    }

    public  GankRetrofitService getService(){
        return gankRetrofitService;
    }

}

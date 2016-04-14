package com.ybh.lovemeizi.http;

/**
 * Created by y on 2016/4/12.
 */
public class GankServiceFactory {
    static final Object obj=new Object();
    private static GankRetrofitService service;

    public static GankRetrofitService getSingleService(){
        synchronized(obj){
            if (null==service) {
                service = new GankRetrofit().getService();
            }
        }
        return service;
    }
}

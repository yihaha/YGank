package com.ybh.lovemeizi.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by y on 2016/5/23.
 */
public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        return proceed
                .newBuilder()
                //缓存2天
                .header("Cache-Control", "public, max-age=" + 60 * 60 * 24 * 2)
                .build();
    }
}

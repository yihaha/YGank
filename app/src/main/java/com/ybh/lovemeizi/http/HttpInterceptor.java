package com.ybh.lovemeizi.http;

import com.ybh.lovemeizi.YApp;
import com.ybh.lovemeizi.utils.NetUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by y on 2016/5/23.
 * 参考:http://www.jianshu.com/p/42a396430be5
 */
public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtil.isConnected(YApp.yContext)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }


        Response response = chain.proceed(request);

        if (NetUtil.isConnected(YApp.yContext)) {
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60) //1分钟
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            //没网缓存3天
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 3)
                    .removeHeader("Pragma")
                    .build();
        }

        return response;
//        return proceed
//                .newBuilder()
//                //缓存10h
//                .header("Cache-Control", "public, max-age=" + 60 * 60 * 10)
//                .build();
    }
}

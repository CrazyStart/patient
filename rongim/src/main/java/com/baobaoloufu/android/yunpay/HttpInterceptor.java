package com.baobaoloufu.android.yunpay;


import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {
    private Context context;

    public HttpInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request oldRequest=chain.request();//旧连接
        Request newRequest=oldRequest.newBuilder()
                .addHeader("x-token",ShareUtils.getValue(context, "token", ""))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        return chain.proceed(newRequest);
    }




}

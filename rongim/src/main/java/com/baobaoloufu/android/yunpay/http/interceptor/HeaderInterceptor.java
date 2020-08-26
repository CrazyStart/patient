package com.baobaoloufu.android.yunpay.http.interceptor;

import androidx.annotation.NonNull;

import com.baobaoloufu.android.yunpay.RongIMWXModule;
import com.baobaoloufu.android.yunpay.ShareUtils;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 统一添加请求头
 */
public class HeaderInterceptor implements Interceptor {


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
//                .addHeader("x-token", RongIMWXModule.getInstance().getToken())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        return chain.proceed(newRequest);
    }
}

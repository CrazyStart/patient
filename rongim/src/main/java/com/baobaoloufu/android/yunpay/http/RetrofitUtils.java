package com.baobaoloufu.android.yunpay.http;


import com.baobaoloufu.android.yunpay.BuildConfig;
import com.baobaoloufu.android.yunpay.api.ApiService;
import com.baobaoloufu.android.yunpay.constant.ApiConstants;
import com.baobaoloufu.android.yunpay.constant.AppConstant;
import com.baobaoloufu.android.yunpay.http.interceptor.HeaderInterceptor;
import com.baobaoloufu.android.yunpay.util.CustomGsonConverterFactory;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Retrofit封装
 */
public class RetrofitUtils {
    private static final String TAG = "RetrofitUtils";
    private static ApiService apiService;

    /**
     * 单例模式
     */
    public static ApiService getApiUrl() {
        if (apiService == null) {
            synchronized (RetrofitUtils.class) {
                if (apiService == null) {
                    apiService = new RetrofitUtils().getRetrofit();
                }
            }
        }
        return apiService;
    }

    private RetrofitUtils() {
    }

    public ApiService getRetrofit() {
        // 初始化Retrofit
        return initRetrofit(initOkHttp()).create(ApiService.class);
    }


    /**
     * 初始化Retrofit
     */
    @NonNull
    private Retrofit initRetrofit(OkHttpClient client) {

        String baseUrl = ApiConstants.TEST
                ? ApiConstants.BASE_URL_DEV
                : ApiConstants.BASE_URL;

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .build();
    }

    /**
     * 初始化okhttp
     */
    @NonNull
    private OkHttpClient initOkHttp() {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //打印网络请求日志
        LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build();
        builder.addInterceptor(httpLoggingInterceptor);
        return builder
                .readTimeout(AppConstant.DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(AppConstant.DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
                .writeTimeout(AppConstant.DEFAULT_TIME, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(new HeaderInterceptor())//统一添加请求头
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();
    }
}
package com.baobaoloufu.android.yunpay.util;

import com.baobaoloufu.android.yunpay.http.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author : jc
 * date : 2020-01-06 17:15
 * description :
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final String TAG = "CustomGsonResponseBodyConverter";
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        BaseResponse httpStatus = gson.fromJson(response, BaseResponse.class);
        //验证status返回是否为1
        if (httpStatus.status == 0) {

            //继续处理body数据反序列化，注意value.string() 不可重复使用
            MediaType contentType = value.contentType();
            Charset charset = (contentType == null)? contentType.charset(UTF_8): UTF_8;
            ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
            InputStreamReader reader = new InputStreamReader(inputStream, charset);
            JsonReader jsonReader = gson.newJsonReader(reader);

            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        } else {
            value.close();
            //不为-1，表示响应数据不正确，抛出异常
            BaseResponse error = new BaseResponse();
            error.status = httpStatus.status;
            error.data = httpStatus.data;
            return (T) error;
        }

    }
}
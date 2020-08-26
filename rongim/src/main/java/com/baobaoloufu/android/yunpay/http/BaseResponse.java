package com.baobaoloufu.android.yunpay.http;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 */
public class BaseResponse <T> implements Serializable {
    public T data;
    public int status;

    public T getModel() {
        return data;
    }

    public void setModel(T model) {
        this.data = model;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
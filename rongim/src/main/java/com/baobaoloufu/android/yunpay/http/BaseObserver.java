package com.baobaoloufu.android.yunpay.http;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        //基础数据 进行统一处理
        if (response.status == 0) {
            onSuccess(response.data);
        }else {
            onFailure(null, "请求失败");
            onFailureCode(response.status, "请求失败");
        }
    }

    public void onFailureCode(int code, String msg) {

    }

    @Override
    public void onError(Throwable e) {
        Log.e("fdsfdsfdsf","========");
        onFailure(e, RxExceptionUtil.exceptionHandler(e));
    }

    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(T demo);

    public abstract void onFailure(Throwable e, String errorMsg);
}
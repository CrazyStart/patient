package com.baobaoloufu.android.yunpay.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import java.io.IOException;

import io.reactivex.disposables.Disposable;

/**
 * Observer加入加载框
 *
 * @param <T>
 */
public abstract class MyObserver<T> extends BaseObserver<T> {
    private Context mContext;
    private Disposable d;

    public MyObserver(Context context, Boolean showDialog) {
        mContext = context;
    }

    public MyObserver(Context context) {
        this(context, true);
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show();
            onFailure(new IOException(), "未连接网络");
            if (!d.isDisposed()) {
                d.dispose();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (!d.isDisposed()) {
            d.dispose();
        }
        super.onError(e);
    }

    @Override
    public void onComplete() {
        if (!d.isDisposed()) {
            d.dispose();
        }
        super.onComplete();
    }

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }
}
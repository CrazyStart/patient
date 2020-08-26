package com.baobaoloufu.android.yunpay;

import android.content.Context;
import android.util.Log;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class SealNotificationReceiver extends PushMessageReceiver {
    public static int count = 0;

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        Log.e("-=-", "[onNotifyMessageArrived] " + pushNotificationMessage.getPushContent());
        count = count + 1;
        AppShortCutUtil.setCount(count, context);
        Log.d("-=-", "===" + count);
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}

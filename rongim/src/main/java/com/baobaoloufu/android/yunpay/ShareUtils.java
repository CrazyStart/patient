package com.baobaoloufu.android.yunpay;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baobaoloufu.android.yunpay.constant.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShareUtils {
    public final static String SETTING = "Setting";

    public static void putValue(Context context, String resKey, String value) {
        Editor sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putString(resKey, value);
        sp.commit();
    }

    public static String getValue(Context context,String resKey, String defValue) {
        SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String value = sp.getString(resKey, defValue);
        return value;
    }

}

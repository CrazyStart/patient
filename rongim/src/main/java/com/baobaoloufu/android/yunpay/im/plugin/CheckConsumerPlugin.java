package com.baobaoloufu.android.yunpay.im.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.baobaoloufu.android.yunpay.ConversationActivity;
import com.baobaoloufu.android.yunpay.DataSynEvent;
import com.baobaoloufu.android.yunpay.R;
import com.baobaoloufu.android.yunpay.SetFeeActivity;
import com.baobaoloufu.android.yunpay.ShareUtils;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

//public class CheckConsumerPlugin implements IPluginModule {
//    private Context mContext;
//    @Override
//    public Drawable obtainDrawable(Context context) {
//        mContext = context;
//        return context.getResources().getDrawable(R.drawable.ic_im_plugin_check_consumer);
//    }
//
//    @Override
//    public String obtainTitle(Context context) {
//        return "患者资料";
//    }
//
//    @Override
//    public void onClick(Fragment fragment, RongExtension rongExtension) {
//
//        String targetId = rongExtension.getTargetId();
//        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
//        String id = ShareUtils.getValue(mContext, "targetId", "");
//        EventBus.getDefault().post(new DataSynEvent("detail", id));
//        if (null != fragment.getActivity()) {
//            fragment.getActivity().finish();
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int i, int i1, Intent intent) {
//
//    }
//}

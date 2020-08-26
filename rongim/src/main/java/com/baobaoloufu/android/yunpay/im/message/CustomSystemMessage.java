package com.baobaoloufu.android.yunpay.im.message;

import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * 自定义系统消息
 */
@MessageTag(value = "EBH:SystemMsg", flag = MessageTag.ISCOUNTED)
public class CustomSystemMessage extends MessageContent {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CustomSystemMessage() {
    }

    // 快速构建消息对象方法
    public static CustomSystemMessage obtain(String content) {
        CustomSystemMessage model = new CustomSystemMessage();
        model.content = content;
        return model;
    }

    protected CustomSystemMessage(Parcel in) {
        this.content = in.readString();
        this.setUserInfo(in.readParcelable(UserInfo.class.getClassLoader()));
    }


    public CustomSystemMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CommandMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setContent(jsonObj.optString("content"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("CommandMessage", "JSONException " + var4.getMessage());
        }

    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(this.content)) {
                jsonObj.put("content", this.content);
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("CommandMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CustomRoomMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeParcelable(this.getUserInfo(), flags);
    }

    public static final Creator<CustomSystemMessage> CREATOR = new Creator<CustomSystemMessage>() {
        @Override
        public CustomSystemMessage createFromParcel(Parcel source) {
            return new CustomSystemMessage(source);
        }

        @Override
        public CustomSystemMessage[] newArray(int size) {
            return new CustomSystemMessage[size];
        }
    };
}

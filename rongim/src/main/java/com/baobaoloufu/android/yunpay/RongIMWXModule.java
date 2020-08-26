package com.baobaoloufu.android.yunpay;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class RongIMWXModule extends WXSDKEngine.DestroyableModule {
    public static RongIMWXModule instance;
    public static RongIMWXModule getInstance() {
        return instance;
    }

    public String getToken() {
        return ShareUtils.getValue(mWXSDKInstance.getContext(), "token", "");
    }
    private JSCallback jsCallback;

    @JSMethod(uiThread = true)
    public void userInfo(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            Context context = mWXSDKInstance.getContext();
            this.jsCallback = jsCallback;
            String token = options.getString("token");
            String id = options.getString("id");
            ShareUtils.putValue(context, "token", token);
            ShareUtils.putValue(context, "userId", id);
            ShareUtils.putValue(context, "name", options.getString("name"));
            ShareUtils.putValue(context, "avatar", options.getString("avatar"));


            jsCallback.invoke("success");
        }
    }

    @JSMethod(uiThread = true)
    public void login(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            String token = options.getString("token");
//            Toast.makeText(mWXSDKInstance.getContext(), token, Toast.LENGTH_SHORT).show();

            if(RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() !=
                    RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED){

                RongIM.connect(token,
                        new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                Toast.makeText(mWXSDKInstance.getContext(), "token参数报错", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(String userid) {
//                            Toast.makeText(mWXSDKInstance.getContext(), "连接成功--"+s, Toast.LENGTH_SHORT).show();

                                String name = ShareUtils.getValue(mWXSDKInstance.getContext(), "name", "");
                                String avatar = ShareUtils.getValue(mWXSDKInstance.getContext(), "avatar", "");
                                RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid,name, Uri.parse(avatar)));
                                RongIM.getInstance().setMessageAttachedUserInfo(true);

                                jsCallback.invoke("success");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(mWXSDKInstance.getContext(), errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }else{
                jsCallback.invoke("success");
            }

            EventBus.getDefault().register(this);

        }
    }

    @JSMethod(uiThread = true)
    public void startRong(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            RongIM.getInstance().startConversation(mWXSDKInstance.getContext(), Conversation.ConversationType.PRIVATE , "123456", "和123456聊天中");

        }
    }

    @JSMethod(uiThread = true)
    public void startTalk(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            String targetId = options.getString("targetId");
            ShareUtils.putValue(mWXSDKInstance.getContext(), "targetId", targetId);
            String name = options.getString("name");
            String expiryDate = options.getString("expiryDate");
            ShareUtils.putValue(mWXSDKInstance.getContext(),"expiryDate", expiryDate);
            int status = options.getIntValue("status");
            ShareUtils.putValue(mWXSDKInstance.getContext(),"status", status+"");
            String avatar = options.getString("avatar");
//            RongIM.getInstance().setMessageAttachedUserInfo(true);

            if(!NotificationsUtils.isNotificationEnabled(mWXSDKInstance.getContext())){
                NotificationsUtils.requestNotify(mWXSDKInstance.getContext());
            }else{
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String s) {
                        return new UserInfo(targetId, name, Uri.parse(avatar));
                    }
                }, true);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo("userId", name, Uri.parse(avatar)));
                RongIM.getInstance().startPrivateChat(mWXSDKInstance.getContext(),targetId, name);

            }

        }
    }


    @JSMethod(uiThread = true)
    public void joinChatRoom(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            String roomId = options.getString("roomId");

            RongIM.getInstance().joinChatRoom(roomId, 50, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    jsCallback.invoke("success");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(mWXSDKInstance.getContext(), errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    @JSMethod(uiThread = true)
    public void quitChatRoom(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            String roomId = options.getString("roomId");

            RongIM.getInstance().quitChatRoom(roomId, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    jsCallback.invoke("success");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(mWXSDKInstance.getContext(), errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    @JSMethod(uiThread = true)
    public void getMsgList(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;

            if (0 != SealNotificationReceiver.count) {
                //角标清空
                SealNotificationReceiver.count = 0;
                AppShortCutUtil.setCount(SealNotificationReceiver.count, mWXSDKInstance.getContext());
            }

            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    if(conversations == null || conversations.size()==0){
                        jsCallback.invoke("");
                    }else{
                        try{
                            List<IMMessage> list = new ArrayList<>();
                            for(int i=0; i<conversations.size(); i++){
                                IMMessage imMessage = new IMMessage();
                                Conversation conversation = conversations.get(i);
                                String senderUserId = conversation.getSenderUserId();
                                if(TextUtils.equals(senderUserId, ShareUtils.getValue(mWXSDKInstance.getContext(), "userId", ""))){
                                    imMessage.setAvatar("");
                                    imMessage.setName("患者");
                                    imMessage.setId(conversation.getTargetId());
                                }else{
                                    if(conversation.getLatestMessage().getUserInfo() != null){
                                        if(conversation.getLatestMessage().getUserInfo().getPortraitUri() != null){
                                            imMessage.setAvatar(conversation.getLatestMessage().getUserInfo().getPortraitUri().toString());
                                        }else{
                                            imMessage.setAvatar("");
                                        }
                                    }else{
                                        imMessage.setAvatar("");
                                    }

                                    if(conversation.getLatestMessage().getUserInfo() != null){
                                        if(!TextUtils.isEmpty(conversation.getLatestMessage().getUserInfo().getName())){
                                            imMessage.setName(conversation.getLatestMessage().getUserInfo().getName());
                                        }else{
                                            imMessage.setName("患者");
                                        }
                                    }else{
                                        imMessage.setName("患者");
                                    }
                                    imMessage.setId(conversation.getSenderUserId());
                                }


                                imMessage.setUnreadMessageCount(conversation.getUnreadMessageCount());

                                if(conversation.getLatestMessage() instanceof TextMessage){
                                    TextMessage textMessage = (TextMessage) conversation.getLatestMessage();
                                    imMessage.setMsgContent(textMessage.getContent());
                                    imMessage.setType(1);
                                }else if(conversation.getLatestMessage() instanceof ImageMessage){
                                    ImageMessage imageMessage = (ImageMessage) conversation.getLatestMessage();
                                    Uri uri = imageMessage.getMediaUrl();
                                    imMessage.setMsgContent(uri.toString());
                                    imMessage.setType(2);
                                }else if(conversation.getLatestMessage() instanceof VoiceMessage){
                                    VoiceMessage voiceMessage = (VoiceMessage) conversation.getLatestMessage();
                                    imMessage.setMsgContent(voiceMessage.getUri().toString());
                                    imMessage.setType(3);
                                }else {
                                    imMessage.setMsgContent(conversation.getLatestMessage().toString());
                                    imMessage.setType(4);
                                }
                                list.add(imMessage);
                            }
                            jsCallback.invoke(new Gson().toJson(list));
                        }catch (Exception e){
                            e.printStackTrace();
                            jsCallback.invoke("");
                        }

                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(mWXSDKInstance.getContext(), errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    @JSMethod(uiThread = true)
    public void sendMessage(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;

            String roomId = options.getString("roomId");
            String msg = options.getString("message");
            TextMessage textMessage = TextMessage.obtain(msg);

            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, roomId, textMessage, null, null, new IRongCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
                    jsCallback.invoke("success");
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(mWXSDKInstance.getContext(), errorCode.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

//            RongIM.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, roomId, textMessage, null, null, new RongIMClient.SendMessageCallback() {
//                @Override
//                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
//                    Toast.makeText(mWXSDKInstance.getContext(), errorCode.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onSuccess(Integer integer) {
//                    Toast.makeText(mWXSDKInstance.getContext(), integer.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
//                @Override
//                public Message onSend(Message message) {
//
//                    return null;
//                }
//
//                @Override
//                public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
//                    jsCallback.invoke("success");
//                    return false;
//                }
//            });


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSynEvent(DataSynEvent event) {
        //手动调用
        if(callBack != null){
            callBack.success(event.getValue());
        }

    }

    public void setCallBack(final CallBack callBack){
        this.callBack = callBack;
    }

    private CallBack callBack;

    interface CallBack{
        void success(String id);
    }

    @JSMethod(uiThread = true)
    public void goToDetail(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            setCallBack(new CallBack() {
                @Override
                public void success(String id) {
                    jsCallback.invokeAndKeepAlive(id.substring(2));
                }
            });

        }
    }

    @JSMethod(uiThread = true)
    public void getConfigUrl(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            jsCallback.invokeAndKeepAlive(BuildConfig.BASE_URL);

        }
    }

    @JSMethod(uiThread = true)
    public void logout(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            jsCallback.invokeAndKeepAlive(BuildConfig.BASE_URL);

        }
    }



    @JSMethod(uiThread = true)
    public void getChatRoomMsg(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;

            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(Message message, int i) {
                    String msg = JSON.toJSONString(message);
                    jsCallback.invokeAndKeepAlive(msg);
                    return false;
                }
            });

//            String roomId = options.getString("roomId");
//            RongIMClient.getInstance().getChatroomHistoryMessages(roomId, 0, 30, RongIMClient.TimestampOrder.RC_TIMESTAMP_ASC,
//                new IRongCallback.IChatRoomHistoryMessageCallback() {
//                   @Override
//                    public void onSuccess(List<Message> messages, long syncTime) {
//                       if(messages == null){
//                           jsCallback.invokeAndKeepAlive("[]");
//                       }else {
//                           jsCallback.invokeAndKeepAlive(JSON.toJSONString(messages));
//                       }
//
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//
//                    }
//            });


        }
    }

    @JSMethod(uiThread = true)
    public void getChatMsg(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;

            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(Message message, int i) {
                    if(message.getConversationType().equals(Conversation.ConversationType.PRIVATE)){
                        if(message.getContent().getUserInfo() == null){
//                            initGetData(mWXSDKInstance.getContext(), message);
                            jsCallback.invokeAndKeepAlive("success");
                            return false;
                        }

                        RongIM.getInstance().refreshUserInfoCache(message.getContent().getUserInfo());
                        jsCallback.invokeAndKeepAlive("success");
                    }
                    return false;
                }
            });

        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 8866:
                    RongIM.getInstance().setCurrentUserInfo((UserInfo) msg.obj);
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                    break;
            }
        }
    };
    @JSMethod(uiThread = true)
    public void dismiss() {
        destroy();
    }


    @Override
    public void destroy() {

    }
}

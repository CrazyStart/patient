package com.baobaoloufu.android.yunpay;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class Tools {
    /**
     * 接收消息的回调
     */
    interface CallBack{
        void success(Message message, int left);
        void failure();
    }
    /**
     * 回话列表消息的回调
     */
    interface ListCallBack{
        void success(String list);
        void failure();
    }

    /**
     * 监听消息
     * @param callBack
     */
    public static void setOnReceiveMessageListener(final CallBack callBack){
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            /**
             * 收到消息的处理。
             *
             * @param message 收到的消息实体。
             * @param left    剩余未拉取消息数目。
             * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
             */
            @Override
            public boolean onReceived(Message message, int left) {
                switch (message.getConversationType()){
                    case PRIVATE:
                        callBack.success(message, left);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
    }

    /**
     * 监听消息
     * @param callBack
     */
    public static void getConversationList(final ListCallBack callBack){
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<Conversation> result = new ArrayList<>();
                if(conversations != null){
                    for(Conversation item : conversations){
                        if(item.getConversationType() == Conversation.ConversationType.PRIVATE){
                            result.add(item);
                        }
                    }
                }

                callBack.success(JSON.toJSONString(result));

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                callBack.failure();
            }
        });
    }

}

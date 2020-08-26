package com.baobaoloufu.android.yunpay.im.message;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.baobaoloufu.android.yunpay.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

@ProviderTag(messageContent = CustomSystemMessage.class ,showPortrait =  false , centerInHorizontal =  true)
public class CustomSystemMessageProvider extends IContainerItemProvider.MessageProvider<CustomSystemMessage> {
    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_custom_system_message, null);
        ViewHolder holder = new ViewHolder();
        holder.mTvContent = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, CustomSystemMessage customSystemMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (null != customSystemMessage) {
            holder.mTvContent.setText(customSystemMessage.getContent());
        }
        //        if (null != customSystemBean) {
//            holder.mTvContent.setText(customSystemBean.getContent().getContent());
//        }else {
//            holder.mTvContent.setText(customSystemMessage.getContent());
//        }
//        holder.mTvContent.setBackground(ContextCompat.getDrawable(mContext,R.drawable.rc_ic_bubble_left));


    }


    @Override
    public Spannable getContentSummary(CustomSystemMessage customSystemMessage) {
        return new SpannableString(customSystemMessage.getContent());//自定义的消息内容展示
    }

    @Override
    public void onItemClick(View view, int i, CustomSystemMessage customSystemMessage, UIMessage uiMessage) {

    }

    class ViewHolder {
        TextView mTvContent;
    }
}

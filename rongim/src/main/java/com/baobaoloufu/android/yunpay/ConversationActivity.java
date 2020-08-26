package com.baobaoloufu.android.yunpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.baobaoloufu.android.yunpay.bean.SingleConsumerBean;
import com.baobaoloufu.android.yunpay.http.MyObserver;
import com.baobaoloufu.android.yunpay.http.RetrofitUtils;
import com.baobaoloufu.android.yunpay.http.RxHelper;
import com.baobaoloufu.android.yunpay.util.TimeUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConversationActivity extends FragmentActivity {
    //顶部倒计时
    private LinearLayout mLayoutTimeDown;

    private SingleConsumerBean mSingleConsumerBean;

    private Context mContext;


    //患者端
    private LinearLayout mEmptyLayout;
    private TextView mTvTime;
    private TextView mTvBottomEmptyStatus;
    private LinearLayout mBottomLayout;

    private TextView mTvStatus1,mTvCount,mTvStatus2;
    private TextView tv_consult_online;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mLayoutTimeDown.setVisibility(View.GONE);
                    handler.removeCallbacksAndMessages(null);
                    getChatStatus();
                    break;
                case 1:
                    mLayoutTimeDown.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.conversation);

        //患者端
        mEmptyLayout = findViewById(R.id.layout_empty);
        mTvTime = findViewById(R.id.tv_empty_time);
        mTvBottomEmptyStatus = findViewById(R.id.tv_bottom_status);
        mBottomLayout = findViewById(R.id.layout_bottom);

        mTvStatus1 = findViewById(R.id.tv_status_1);
        mTvCount = findViewById(R.id.tv_count);
        mTvStatus2 = findViewById(R.id.tv_status_2);

        tv_consult_online = findViewById(R.id.tv_consult_online);
        tv_consult_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ShareUtils.getValue(ConversationActivity.this, "targetId", "");
                EventBus.getDefault().post(new DataSynEvent("detail", id + "#" + "1"));
                finish();
            }
        });

        mContext = this;
        ImageView backIcon = findViewById(R.id.back_icon);
        TextView title = findViewById(R.id.title);
        ImageView rightIcon = findViewById(R.id.right_icon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText(getIntent().getData().getQueryParameter("title"));

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPop(rightIcon);
            }
        });

//        initGetData("chat");


        Tools.setOnReceiveMessageListener(new Tools.CallBack() {
            @Override
            public void success(io.rong.imlib.model.Message message, int left) {
                getChatStatus();
            }

            @Override
            public void failure() {

            }
        });
//
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public io.rong.imlib.model.Message onSend(io.rong.imlib.model.Message message) {
                return message;
            }

            @Override
            public boolean onSent(io.rong.imlib.model.Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                if (null == message) {
                    return false;
                }
                if (!"EBH:SystemMsg".equals(message.getObjectName()) && !"RC:Tip".equals(message.getObjectName())) {
                    RetrofitUtils.getApiUrl().addConsumerStatus(ShareUtils.getValue(mContext, "token", ""), ShareUtils.getValue(mContext, "targetId", ""))
                            .compose(RxHelper.observableIO2Main(mContext))
                            .subscribe(new MyObserver<Object>(mContext) {
                                @Override
                                public void onSuccess(Object demo) {

                                }

                                @Override
                                public void onFailure(Throwable e, String errorMsg) {

                                }
                            });
                }
                return false;
            }
        });
//        int status = Integer.valueOf(ShareUtils.getValue(this,"status", "")) ;
        getChatStatus();
//        (ConversationFragment)(getSupportFragmentManager().findFragmentById(R.id.conversation))

    }


    private void initGetData(String url) {
//        if(progressDialog == null){
//            progressDialog = new ProgressDialog(this);
//        }
//        if (!progressDialog.isShowing()) {
//            progressDialog.show();
//        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //公共请求头
        HttpInterceptor httpInterceptor = new HttpInterceptor(this);

        builder.addInterceptor(httpInterceptor);
        OkHttpClient client = builder.build();

        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL + "/sapi/rongyun/" + url + "?to=" + ShareUtils.getValue(this, "targetId", ""))
                .build();

        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }

                String result = response.body().string();
                Log.i("result", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt("status");
                    if (status == 0) {
                        Message message = handler.obtainMessage(0);
                        if (TextUtils.equals(url, "chat")) {
                            message = handler.obtainMessage(1);
                            JSONObject data = object.getJSONObject("data");
                            message.obj = data.getLong("expiryDate") + "";
                        }
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setPop(View rightIcon) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
        PopupWindow window = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView changeFee = contentView.findViewById(R.id.change_fee);
        TextView clearChat = contentView.findViewById(R.id.clear_chat);

        changeFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                String id = ShareUtils.getValue(ConversationActivity.this, "targetId", "");
                EventBus.getDefault().post(new DataSynEvent("detail", id + "#" + "1"));
                finish();
//                Intent intent = new Intent();
//                intent.setAction("com.rong.setting");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivity(intent);

//                Intent intent = new Intent(mContext, ConsultRecordActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("avatar", mSingleConsumerBean.getAvatar());
//                bundle.putString("name", mSingleConsumerBean.getName());
//                intent.putExtras(bundle);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivity(intent);
//                intent.setAction("com.rong.setting");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivity(intent);
            }
        });

        clearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                String id = ShareUtils.getValue(ConversationActivity.this, "targetId", "");
                EventBus.getDefault().post(new DataSynEvent("detail", id));
                finish();

            }
        });


        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAsDropDown(rightIcon, 20, -30);
    }


    public void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //获取聊天状态
    private void getChatStatus() {
        RetrofitUtils.getApiUrl().getChatStatus(ShareUtils.getValue(mContext, "token", ""), ShareUtils.getValue(mContext, "targetId", ""))
                .compose(RxHelper.observableIO2Main(mContext))
                .subscribe(new MyObserver<SingleConsumerBean>(mContext) {
                    @Override
                    public void onSuccess(SingleConsumerBean singleConsumerBean) {
                        if (null == singleConsumerBean) {
                            return;
                        }
                        mSingleConsumerBean = singleConsumerBean;
                        refreshBottomView(singleConsumerBean);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                    }
                });
    }

    private void refreshBottomView(SingleConsumerBean singleConsumerBean) {
        switch (singleConsumerBean.getStatus()) {
            case 0://新患者
                mEmptyLayout.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.VISIBLE);
                mTvBottomEmptyStatus.setVisibility(View.GONE);
                mTvTime.setText(TimeUtils.getDate2String(System.currentTimeMillis(),"MM月dd日 HH:mm"));
                break;
            case 1://医生赠送服务中
            case 5:
                mEmptyLayout.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.GONE);
                mTvBottomEmptyStatus.setVisibility(View.GONE);

                mTvStatus1.setVisibility(View.VISIBLE);
                mTvStatus1.setText("在线问诊中，还有");
                mTvCount.setVisibility(View.VISIBLE);
                mTvCount.setText(String.valueOf(mSingleConsumerBean.getRemain_count()));
                mTvStatus2.setVisibility(View.VISIBLE);
                mTvStatus2.setText("次回复");
                break;
            case 2://赠送患者1天机会
                mEmptyLayout.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.GONE);
                mTvBottomEmptyStatus.setVisibility(View.GONE);

                mTvStatus1.setVisibility(View.VISIBLE);
                mTvStatus1.setText("在线问诊中");
                mTvCount.setVisibility(View.GONE);
                mTvStatus2.setVisibility(View.GONE);
                break;
            case 3:
                switch (singleConsumerBean.getType()) {
                    case 1:
                        mEmptyLayout.setVisibility(View.GONE);
                        mBottomLayout.setVisibility(View.GONE);
                        mTvBottomEmptyStatus.setVisibility(View.GONE);

                        mTvStatus1.setVisibility(View.VISIBLE);
                        mTvStatus1.setText("在线问诊中");
                        mTvCount.setVisibility(View.GONE);
                        mTvStatus2.setVisibility(View.GONE);
                        break;
                    case 2:
                    case 3:
                        mEmptyLayout.setVisibility(View.GONE);
                        mBottomLayout.setVisibility(View.GONE);
                        mTvBottomEmptyStatus.setVisibility(View.GONE);

                        mTvStatus1.setVisibility(View.VISIBLE);
                        mTvStatus1.setText("在线问诊中，还有");
                        mTvCount.setVisibility(View.VISIBLE);
                        mTvCount.setText(String.valueOf(mSingleConsumerBean.getRemain_count()));
                        mTvStatus2.setVisibility(View.VISIBLE);
                        mTvStatus2.setText("次回复");
                        break;

                }
                break;
            case 4://咨询结束
                mEmptyLayout.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.VISIBLE);
                mTvBottomEmptyStatus.setVisibility(View.VISIBLE);
                break;
        }
    }

}

package com.baobaoloufu.android.yunpay;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingFeeActivity extends FragmentActivity {

    LinearLayout bottomLayout;
    RelativeLayout settingLayout;
    EditText editText;
    TextView sureBtn, inputValue;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    inputValue.setText("￥"+(String) msg.obj+"/次");
                    break;

                case 1:
                    Toast.makeText(SettingFeeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    inputValue.setText("￥"+editText.getText().toString()+"/次");
                    editText.setText("");
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting_fee);

        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        settingLayout = findViewById(R.id.setting_layout);
        editText = findViewById(R.id.edit_num);
        sureBtn = findViewById(R.id.sure_btn);

        final View myLayout = getWindow().getDecorView();
        RelativeLayout parentLayout = findViewById(R.id.root);

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int statusBarHeight;
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                // 使用最外层布局填充，进行测算计算
                parentLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = myLayout.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                if (heightDiff > 100) {
                    // 如果超过100个像素，它可能是一个键盘。获取状态栏的高度
                    statusBarHeight = 0;
                }
                try {
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int realKeyboardHeight = heightDiff - statusBarHeight;
                if(realKeyboardHeight > 500){
                    bottomLayout.setVisibility(View.VISIBLE);
                }else{
                    bottomLayout.setVisibility(View.GONE);
                }
                Log.e("键盘", "keyboard height(单位像素) = " + realKeyboardHeight);
            }
        });

        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomLayout.getVisibility() == View.GONE){
                    bottomLayout.setVisibility(View.VISIBLE);
                    showInput(editText);
                }else{
                    hideInput();
                }

            }
        });

        inputValue = findViewById(R.id.input_value);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString())){

                    hideInput();

                    initPostData();
                }
            }
        });

        initGetData();
    }

    private void initPostData(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //公共请求头
        HttpInterceptor httpInterceptor = new HttpInterceptor(this);

        builder.addInterceptor(httpInterceptor);
        OkHttpClient client = builder.build();


        //第二步创建RequestBody（Form表达）
        RequestBody body = new FormBody.Builder()
                .add("price", editText.getText().toString())
                .add("consumer", ShareUtils.getValue(this, "targetId", ""))
                .build();
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+ "/sapi/hcp/servicePrice")
                .put(body)
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
                String result = response.body().string();
                Log.i("result", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt("status");
                    if(status == 0){
                        String data = object.getString("data");
                        Message message = handler.obtainMessage(1,"");
                        handler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initGetData(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //公共请求头
        HttpInterceptor httpInterceptor = new HttpInterceptor(this);

        builder.addInterceptor(httpInterceptor);
        OkHttpClient client = builder.build();

        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+ "/sapi/hcp/servicePrice?consumer="+ShareUtils.getValue(this, "targetId", ""))
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
                String result = response.body().string();
                Log.i("result", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt("status");
                    if(status == 0){
                        String data = object.getString("data");
                        String price = new JSONObject(data).getString("price");
                        Message message = handler.obtainMessage(0,price);
                        handler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        bottomLayout.setVisibility(View.GONE);
    }

}

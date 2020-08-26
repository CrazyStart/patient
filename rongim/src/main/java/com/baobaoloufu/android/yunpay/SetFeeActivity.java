package com.baobaoloufu.android.yunpay;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baobaoloufu.android.yunpay.bean.PriceBean;
import com.baobaoloufu.android.yunpay.dialog.ChangePriceDialog;
import com.baobaoloufu.android.yunpay.http.MyObserver;
import com.baobaoloufu.android.yunpay.http.RetrofitUtils;
import com.baobaoloufu.android.yunpay.http.RxHelper;
import com.baobaoloufu.android.yunpay.util.GlideUtils;
import com.baobaoloufu.android.yunpay.util.JsonUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SetFeeActivity extends FragmentActivity {
    private Context mContext;
    private TextView tv_price_1,tv_price_2,tv_price_3;
    private TextView tv_change_price_1,tv_change_price_2,tv_change_price_3;
    private LinearLayout layout_back;

    private PriceBean mPriceBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_fee);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            TextView tv_name = findViewById(R.id.tv_name);
            tv_name.setText(new StringBuilder().append("患者：").append(bundle.getString("name")));
            ImageView iv_avatar = findViewById(R.id.iv_avatar);
            GlideUtils.loadImage(mContext,bundle.getString("avatar"),iv_avatar);
        }

        tv_price_1 = findViewById(R.id.tv_price_1);
        tv_price_2 = findViewById(R.id.tv_price_2);
        tv_price_3 = findViewById(R.id.tv_price_3);

        layout_back = findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mContext = this;
    }


}
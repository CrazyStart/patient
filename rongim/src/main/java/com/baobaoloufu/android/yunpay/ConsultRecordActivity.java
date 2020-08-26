package com.baobaoloufu.android.yunpay;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * 咨询记录
 */
public class ConsultRecordActivity extends FragmentActivity {
    private LinearLayout layout_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_consult_record);
        bindView();
        setListener();
    }

    private void bindView() {
        layout_title.findViewById(R.id.layout_title);
    }

    private void setListener() {
        layout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
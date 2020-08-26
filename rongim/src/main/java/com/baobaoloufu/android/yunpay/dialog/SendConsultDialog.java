package com.baobaoloufu.android.yunpay.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.baobaoloufu.android.yunpay.R;

public class SendConsultDialog extends DialogFragment {
    private Context mContext;
    private int mType = 1;

    private OnDialogClickListener mOnDialogClickListener;

    public interface OnDialogClickListener {
        void onCancelClick();
        void onConfirmClick(int type);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public SendConsultDialog(Context mContext, OnDialogClickListener onDialogClickListener) {
        this.mContext = mContext;
        this.mOnDialogClickListener = onDialogClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog loadingDialog = new Dialog(mContext);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (null != loadingDialog.getWindow()) {
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnim;//动画
            //设置全屏
            Window window = loadingDialog.getWindow();
            loadingDialog.setCanceledOnTouchOutside(false);


            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_send_consult,null);
            LinearLayout layout_send_1 = view.findViewById(R.id.layout_send_1);
            LinearLayout layout_send_2 = view.findViewById(R.id.layout_send_2);
            LinearLayout layout_send_3 = view.findViewById(R.id.layout_send_3);
            layout_send_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mType = 1;
                    layout_send_1.setBackgroundColor(Color.parseColor("#F0F0FB"));
                    layout_send_2.setBackgroundColor(Color.parseColor("#ffffff"));
                    layout_send_3.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            });
            layout_send_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mType = 2;
                    layout_send_1.setBackgroundColor(Color.parseColor("#ffffff"));
                    layout_send_2.setBackgroundColor(Color.parseColor("#F0F0FB"));
                    layout_send_3.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            });
            layout_send_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mType = 3;
                    layout_send_1.setBackgroundColor(Color.parseColor("#ffffff"));
                    layout_send_2.setBackgroundColor(Color.parseColor("#ffffff"));
                    layout_send_3.setBackgroundColor(Color.parseColor("#F0F0FB"));
                }
            });

            TextView cancel_tv = view.findViewById(R.id.cancel_tv);
            cancel_tv.setOnClickListener(v -> {
                dismiss();
                mOnDialogClickListener.onCancelClick();
            });

            TextView confirm_tv = view.findViewById(R.id.confirm_tv);
            confirm_tv.setOnClickListener(v -> {
                dismiss();
                mOnDialogClickListener.onConfirmClick(mType);
            });

            loadingDialog.setContentView(view);
        }
        return loadingDialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.baobaoloufu.android.yunpay.R;

public class ChangePriceDialog extends DialogFragment {
    private Context mContext;
    private int mType = 1;

    private OnDialogClickListener mOnDialogClickListener;

    public interface OnDialogClickListener {
        void onCancelClick();
        void onConfirmClick(String price);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public ChangePriceDialog(Context mContext, OnDialogClickListener onDialogClickListener) {
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
            //设置全屏
            Window window = loadingDialog.getWindow();
            loadingDialog.setCanceledOnTouchOutside(false);


            int type = 1;
            if (null != getArguments()) {
                type = getArguments().getInt("type");
            }

            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_change_price,null);

            TextView title_tv = view.findViewById(R.id.title_tv);
            TextView tv_sugess_price = view.findViewById(R.id.tv_sugess_price);
            switch (type) {
                case 1:
                    title_tv.setText("设置图文咨询价格");
                    tv_sugess_price.setText("建议价格：30～60元");
                    break;
                case 2:
                    title_tv.setText("设置追问价格");
                    tv_sugess_price.setText("建议价格：20～30元");
                    break;
                case 3:
                    title_tv.setText("设置一问一答价格");
                    tv_sugess_price.setText("建议价格：10～20元");
                    break;
            }
            EditText edit_price = view.findViewById(R.id.edit_price);

            TextView cancel_tv = view.findViewById(R.id.cancel_tv);
            cancel_tv.setOnClickListener(v -> {
                dismiss();
                mOnDialogClickListener.onCancelClick();
            });

            TextView confirm_tv = view.findViewById(R.id.confirm_tv);
            confirm_tv.setOnClickListener(v -> {
                dismiss();
                String inputPrice = edit_price.getText().toString();
                if (inputPrice.equals("")) {
                    Toast.makeText(mContext,"请输入价格",Toast.LENGTH_SHORT).show();
                    return;
                }
                mOnDialogClickListener.onConfirmClick(inputPrice);
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
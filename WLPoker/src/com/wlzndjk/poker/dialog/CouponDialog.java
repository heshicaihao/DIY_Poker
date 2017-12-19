package com.wlzndjk.poker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.wlzndjk.poker.R;

public class CouponDialog {

	private Button btn_cancel;
	private Button btn_confirm;

	private Context context;
	private CouponCallBack callBack;
	private String callBackData;
	private EditText mCouponEt;

	public CouponCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(CouponCallBack callBack) {
		this.callBack = callBack;
	}

	public interface CouponCallBack {
		public void isConfirm(String callBackData);
	}

	public CouponDialog(Context mcontext) {
		this.context = mcontext;
	}

	private void setDialogLayoutParams(Dialog builder) {
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.height = LayoutParams.WRAP_CONTENT; // 高度
		// lp.alpha = 0.7f; // 透明度
		lp.width = LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
	}

	public Dialog onCreateDialog() {
		final View view = View.inflate(context, R.layout.dialog_coupon,
				null);

		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
		mCouponEt = (EditText) view.findViewById(R.id.coupon_et);
		
		final Dialog builder = new Dialog(context, R.style.Theme_dialog);
		builder.setContentView(view);
		setDialogLayoutParams(builder);
		builder.show();

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBackData =  mCouponEt.getText().toString().trim();
					callBack.isConfirm(callBackData);
				}
				builder.dismiss();
			}
		});
		return builder;
	}

}
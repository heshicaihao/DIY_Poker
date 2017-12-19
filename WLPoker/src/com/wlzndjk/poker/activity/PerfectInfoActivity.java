package com.wlzndjk.poker.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.base.BaseActivity;
import com.wlzndjk.poker.bean.UserBean;
import com.wlzndjk.poker.common.UserController;
import com.wlzndjk.poker.net.NetHelper;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.LogUtils;
import com.wlzndjk.poker.utils.SharedpreferncesUtil;
import com.wlzndjk.poker.utils.StringUtils;
import com.wlzndjk.poker.utils.ToastUtils;

/**
 * 完善信息页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class PerfectInfoActivity extends BaseActivity implements
		OnClickListener {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mPromoCodeET;
	private Button mRegisterBt;
	private Button mJumpBt;

	private UserBean mUser;
	private String mAccountId;
	private String mPromoteCode;

	// private InputMethodManager mImm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfect_info);
		mUser = UserController.getInstance(this).getUserInfo();
		initView();
		initData();

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			goBack();

			break;
		case R.id.register:
			mAccountId = mUser.getId();
			mPromoteCode = mPromoCodeET.getText().toString().trim();
			if (StringUtils.isEmpty(mPromoteCode)) {
				ToastUtils.show(R.string.please_input_promote_code_null);
			} else {
				ApplyPromoter(mAccountId, mPromoteCode);
			}
			break;

		case R.id.jump:
			// mImm.hideSoftInputFromWindow(this.getCurrentFocus()
			// .getWindowToken(), 0);
			// AndroidUtils.finishActivity(this);

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
		}
		return false;
	}

	private void goBack() {
		logout();
		AndroidUtils.finishActivity(this);
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mPromoCodeET = (EditText) findViewById(R.id.promo_code_edit_text);
		mRegisterBt = (Button) findViewById(R.id.register);
		mJumpBt = (Button) findViewById(R.id.jump);

		mTitle.setText(R.string.perfect_info);
		mBack.setOnClickListener(this);
		mJumpBt.setOnClickListener(this);
		mRegisterBt.setOnClickListener(this);
	}

	private void initData() {
		mAccountId = mUser.getId();
		mPromoteCode = mPromoCodeET.getText().toString().trim();

	}

	/**
	 * 申请 成为推广人 有推广码
	 * 
	 * @param mAccountId
	 * @param mPromoteCode
	 */
	private void ApplyPromoter(String account_id, String promote_code) {
		NetHelper.setPromoter(account_id, promote_code,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "setPromoter有推广码+onSuccess");
						resolvesetPromoter(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "setPromoter有推广码+onFailure");
					}
				});
	}

	/**
	 * 解析申请推广数据
	 * 
	 * @param arg2
	 */
	protected void resolvesetPromoter(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			// String msg = obj.optString("msg");
			if ("0".equals(code)) {
				SharedpreferncesUtil.setProofLogin(getApplicationContext(), true);
				PerfectInfoActivity.this.finish();
			} else {
				// ToastUtils.shortShow(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

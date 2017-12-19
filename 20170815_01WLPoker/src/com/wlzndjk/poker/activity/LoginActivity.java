package com.wlzndjk.poker.activity;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.base.BaseActivity;
import com.wlzndjk.poker.bean.QQInfo;
import com.wlzndjk.poker.bean.SinaInfo;
import com.wlzndjk.poker.bean.WeixinInfo;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.dialog.CustomProgressDialog;
import com.wlzndjk.poker.net.NetHelper;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.FileUtil;
import com.wlzndjk.poker.utils.LogUtils;
import com.wlzndjk.poker.utils.SharedpreferncesUtil;
import com.wlzndjk.poker.utils.ToastUtils;
import com.wlzndjk.poker.widget.SwitchView;
import com.wlzndjk.poker.widget.SwitchView.OnStateChangedListener;

/**
 * 登录页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends BaseActivity implements OnClickListener,
		OnStateChangedListener {

	private static final int REQUESTCODE = 1;
	private static final int REQUESTCODE02 = 2;
	private String mPhoneStr;
	private String mPasswordStr;

	private TextView mTitle;
	private TextView mSave;
	private ImageView mBack;
	private TextView mRegister;
	private TextView mFindPassword;
	private ImageView mAccountDelete;
	private ImageView mPassDelete;
	private ImageView mQQ;
	private ImageView mWechat;
	private ImageView mSina;

	private EditText mAccount;
	private EditText mPassword;
	private Button mLogin;

	private Gson gson;

	private UMShareAPI mShareAPI = null;
	private String mOpenid;
	private String mType;
	private String mUserName;
	private boolean mIsThreeLogin = false;
	private CustomProgressDialog mDialog;
	private SwitchView mSwitchView;
	private String mHead_portrait;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.save:
			startActivity(this, RegisterActivity.class);
			finish();
			break;
		case R.id.register:
			startActivity(this, RegisterActivity.class);
			finish();
			break;
		case R.id.find_password:
			startActivity(this, ResetPasswordActivity.class);
			finish();
			break;
		case R.id.qq_login:
			mType = "qzone";
			getOauth(SHARE_MEDIA.QQ, mType);
			break;
		case R.id.wechat_login:
			mType = "wechat";
			getOauth(SHARE_MEDIA.WEIXIN, mType);
			break;
		case R.id.sina_login:
			mType = "sina";
			getOauth(SHARE_MEDIA.SINA, mType);
			break;
		case R.id.account_delete:
			mAccount.setText("");
			mAccountDelete.setVisibility(View.GONE);
			break;
		case R.id.password_delete:
			mPassword.setText("");
			mPassDelete.setVisibility(View.GONE);
			break;
		case R.id.login:
			if (AndroidUtils.isNoFastClick()) {
				getInfoInput();
				mDialog.show();
				login();
			}
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUESTCODE:
			if (resultCode == RESULT_OK) {
				mPhoneStr = data.getStringExtra("mPhoneStr");
				mAccount.setText(mPhoneStr);
			}
			break;
		case REQUESTCODE02:
			if (resultCode == RESULT_OK) {
				mPhoneStr = data.getStringExtra("mPhoneStr");
				mAccount.setText(mPhoneStr);
			}
			break;
		default:
			break;
		}
	}

	private void getInfoInput() {
		mPhoneStr = mAccount.getText().toString().trim();
		mPasswordStr = mPassword.getText().toString().trim();

	}

	@Override
	public void toggleToOn() {
		mSwitchView.toggleSwitch(true);
		mPassword.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
		mPassword.setSelection(mPasswordStr.length());
	}

	@Override
	public void toggleToOff() {
		mSwitchView.toggleSwitch(false);
		mPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		mPassword.setSelection(mPasswordStr.length());
	}

	private void login() {
		if (StringUtils.isEmpty(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_null);
			mDialog.dismiss();
			return;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			ToastUtils.show(R.string.please_input_phone_again);
			mAccount.setText("");
			mPhoneStr = null;
			mDialog.dismiss();
			return;
		}
		if (StringUtils.isEmpty(mPasswordStr)) {
			ToastUtils.show(R.string.please_input_password_null);
			mDialog.dismiss();
			return;
		}
		NetHelper.loginUser(mPhoneStr, mPasswordStr,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "loginUser +onSuccess");
						resolveLoginUser(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						ToastUtils.show(R.string.request_service_failure);
						LogUtils.logd(TAG, "loginUser +onFailure");
						mDialog.dismiss();
					}
				});
	}

	private void resolveLoginUser(byte[] responseBody) {
		String json;
		try {
			json = new String(responseBody, "UTF-8");
			JSONObject obj = new JSONObject(json);
			String msg = obj.optString("msg");
			LogUtils.logd(TAG, "msg:" + msg);
			LogUtils.logd(TAG, "json:" + json);
			String code = obj.optString("code");
			JSONObject result = obj.optJSONObject("result");
			LogUtils.logd(TAG, "code:" + code);
			if ("0".equals(code)) {
				mIsThreeLogin = false;
				savaLoginInfo(result);
			} else {
				ToastUtils.show(msg);
				mDialog.dismiss();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void initView() {

		mTitle = (TextView) findViewById(R.id.title);
		mSave = (TextView) findViewById(R.id.save);
		mBack = (ImageView) findViewById(R.id.back);
		mRegister = (TextView) findViewById(R.id.register);
		mFindPassword = (TextView) findViewById(R.id.find_password);
		mAccount = (EditText) findViewById(R.id.account);
		mPassword = (EditText) findViewById(R.id.password);
		mAccountDelete = (ImageView) findViewById(R.id.account_delete);
		mPassDelete = (ImageView) findViewById(R.id.password_delete);
		mQQ = (ImageView) findViewById(R.id.qq_login);
		mWechat = (ImageView) findViewById(R.id.wechat_login);
		mSina = (ImageView) findViewById(R.id.sina_login);
		mLogin = (Button) findViewById(R.id.login);
		mSwitchView = (SwitchView) findViewById(R.id.view_switch);

		mTitle.setText(R.string.account_login);
		mSave.setVisibility(View.VISIBLE);
		mSave.setText(R.string.register);
		mAccountDelete.setVisibility(View.GONE);
		mPassDelete.setVisibility(View.GONE);

		setTextChangedListener();
		mRegister.setOnClickListener(this);
		mFindPassword.setOnClickListener(this);
		mAccountDelete.setOnClickListener(this);
		mPassDelete.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mQQ.setOnClickListener(this);
		mWechat.setOnClickListener(this);
		mSina.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mSwitchView.setOnStateChangedListener(this);

		mDialog = new CustomProgressDialog(this, this.getResources().getString(
				R.string.dialog_info_login));

	}

	private void initData() {
		mShareAPI = UMShareAPI.get(this);
	}

	/**
	 * 添加编辑框监听
	 */
	private void setTextChangedListener() {
		mAccount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mAccountDelete.setVisibility(View.VISIBLE);
				} else {
					mAccountDelete.setVisibility(View.GONE);
				}
			}
		});
		mPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				getInfoInput();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mPassDelete.setVisibility(View.VISIBLE);
				} else {
					mPassDelete.setVisibility(View.GONE);
				}
			}
		});
	}

	private void otherLogin(String openid, String type, String username) {
		NetHelper.threePartyLogin(openid, type, username,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "threePartyLogin +onSuccess");
						resolveOtherLogin(arg2);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						mDialog.dismiss();
						LogUtils.logd(TAG, "threePartyLogin +onFailure");
					}
				});
	}

	private void resolveOtherLogin(byte[] responseBody) {
		String json;
		try {

			json = new String(responseBody, "UTF-8");
			JSONObject obj = new JSONObject(json);

			LogUtils.logd(TAG, "responseBody:" + json);
			String msg = obj.optString("msg");
			LogUtils.logd(TAG, "msg:" + msg);
			String code = obj.optString("code");
			JSONObject result = obj.optJSONObject("result");
			if ("0".equals(code)) {
				mIsThreeLogin = true;
				savaLoginInfo(result);
			} else {
				ToastUtils.show(msg);
				mDialog.dismiss();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getOauth(final SHARE_MEDIA platform, final String type) {
		mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
			@Override
			public void onComplete(SHARE_MEDIA arg2, int action,
					Map<String, String> data) {
				// ToastUtils.show("Authorize succeed");
				mDialog.show();
				getUserInfo(platform, type);
			}

			@Override
			public void onError(SHARE_MEDIA platform, int action, Throwable t) {
				// ToastUtils.show("Authorize fail");
			}

			@Override
			public void onCancel(SHARE_MEDIA platform, int action) {
				// ToastUtils.show("Authorize cancel");
			}
		});
	}

	private void getUserInfo(SHARE_MEDIA platform, final String type) {
		mShareAPI.getPlatformInfo(LoginActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onCancel(SHARE_MEDIA arg0, int arg1) {
						// ToastUtils.show("getUserInfo+onCancel");
						mDialog.dismiss();
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int status,
							Map<String, String> info) {
						// ToastUtils.show("getUserInfo+onComplete:info"
						// + info.toString());
						if (info != null) {
							gson = new Gson();
							String json = gson.toJson(info);
							if ("qzone".equals(type)) {
								QQInfo qq = gson.fromJson(json, QQInfo.class);
								mOpenid = qq.getOpenid();
								mUserName = qq.getScreen_name();
								mHead_portrait = qq.getProfile_image_url();
							} else if ("wechat".equals(type)) {
								WeixinInfo wx = gson.fromJson(json,
										WeixinInfo.class);
								mOpenid = wx.getOpenid();
								mUserName = wx.getNickname();
								mHead_portrait = wx.getHeadimgurl();
							} else if ("sina".equals(type)) {
								SinaInfo sina = gson.fromJson(json,
										SinaInfo.class);
								mOpenid = sina.getUid();
								mUserName = sina.getScreen_name();
								mHead_portrait = sina.getProfile_image_url();
							}
							otherLogin(mOpenid, type, mUserName);
						}

					}

					@Override
					public void onError(SHARE_MEDIA arg0, int arg1,
							Throwable arg2) {
						// ToastUtils.show("getUserInfo+onError");
						mDialog.dismiss();
					}

				});
	}

	/**
	 * 登录成功后，保存用户信息
	 * 
	 * @param result
	 */
	private void savaLoginInfo(JSONObject result) {

		//
		JSONObject accountinfo = result.optJSONObject("account_info");
		String token = accountinfo.optString("token");
		String id = accountinfo.optString("id");

		user.setUname(mPhoneStr);
		user.setPassword(mPasswordStr);
		user.setId(id);
		user.setToken(token);
		user.setIs_login(true);
		user.setType(mType);
		user.setOpen_id(mOpenid);
		user.setUsername(mUserName);
		user.setHead_portrait(mHead_portrait);
		user.setIs_three_login(mIsThreeLogin);

		if (!SharedpreferncesUtil.getAlias(getApplication())) {
			LogUtils.logd(TAG, id);
			JPushInterface.setAliasAndTags(getApplicationContext(), id, null,
					new TagAliasCallback() {

						@Override
						public void gotResult(int code, String alias,
								Set<String> tags) {
							LogUtils.log(TAG, "alias:"+alias);
						}

					});
		}
		getOrderInfo();
	}

	private void getOrderInfo() {
		LogUtils.logd(TAG, "user.getId()" + user.getId());
		LogUtils.logd(TAG, "user.getToken()" + user.getToken());

		NetHelper.getOrderList(user.getId(), user.getToken(), "all", 1, 5,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getOrderList+onSuccess");
						resolveOrderList(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getOrderList+onFailure");
						mDialog.dismiss();
					}
				});
	}

	private void resolveOrderList(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "arr== nulljson:" + json);
			JSONObject obj = new JSONObject(json);
			JSONObject result = obj.optJSONObject("result");
			JSONArray arr = result.optJSONArray("orderData");
			if (arr != null && arr.length() != 0) {
				FileUtil.saveFile(arr.toString(), MyConstants.ORDERLIST,
						MyConstants.ORDER_LIST_INFO, MyConstants.TXT);
				SharedpreferncesUtil.setOrder(getApplication(), true);
			} else {
				SharedpreferncesUtil.setOrder(getApplication(), false);
			}
			mUserController.saveUserInfo(user);
			mDialog.dismiss();
			if (mIsThreeLogin) {
				initPopularizeinfo(user.getId());
			} else {
				ToastUtils.show(R.string.login_ok);
				finish();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			mDialog.dismiss();
		} catch (JSONException e) {
			e.printStackTrace();
			mDialog.dismiss();
		}
	}


	private void initPopularizeinfo(String account_id) {
		NetHelper.getPromoteInfo(account_id, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "initPopularizeinfo+onSuccess");
				resolvePromoteInfo(arg2);

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initPopularizeinfo+onFailure");

			}
		});

	}

	/**
	 * 解析 我的推广信息
	 * 
	 * @param arg2
	 */
	private void resolvePromoteInfo(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			if ("0".equals(code)) {
				JSONObject result = obj.optJSONObject("result");
				showExecute(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 显示 提示项
	 * 
	 * @param result
	 */
	private void showExecute(JSONObject result) {
		boolean mIsPromoter = result.optBoolean("is_promoter", false);
		if (!mIsPromoter) {
			// 不是推广人 并且没有申请推广码
			startActivity(this, PerfectInfoActivity.class);
			finish();
		} else {
			SharedpreferncesUtil.setProofLogin(getApplicationContext(), true);
			ToastUtils.show(R.string.login_ok);
			finish();
		}
	}

}

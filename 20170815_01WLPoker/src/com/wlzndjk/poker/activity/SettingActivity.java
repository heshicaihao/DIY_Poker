package com.wlzndjk.poker.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.base.BaseActivity;
import com.wlzndjk.poker.bean.UserBean;
import com.wlzndjk.poker.common.UserController;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.net.MyURL;
import com.wlzndjk.poker.net.NetHelper;
import com.wlzndjk.poker.update.UpdateManager;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.FileUtil;
import com.wlzndjk.poker.utils.LogUtils;
import com.wlzndjk.poker.utils.StringUtils;

/**
 * 
 * 设置界面
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private LinearLayout mHelpLL;
	private LinearLayout mAppUpdateLL;
	private LinearLayout mShareLL;
	private LinearLayout mSuggestionsLL;
	private LinearLayout mContactUsLL;
	private Button mLogoutLL;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();
		initData();
	}

	@Override
	public void onResume() {
		super.onResume();
		mUser = UserController.getInstance(this).getUserInfo();
		if (mUser.isIs_login()) {
			String promote_code_info_path = FileUtil.getFilePath(
					MyConstants.PCODE, MyConstants.PCODE_INFO + mUser.getId(),
					MyConstants.JSON);
			if (!FileUtil.fileIsExists(promote_code_info_path)) {
				initPopularizeinfo();
			}
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mLogoutLL = (Button) findViewById(R.id.logout_btn);
		mHelpLL = (LinearLayout) findViewById(R.id.help_ll);
		mAppUpdateLL = (LinearLayout) findViewById(R.id.app_update_ll);
		mShareLL = (LinearLayout) findViewById(R.id.app_share_ll);
		mSuggestionsLL = (LinearLayout) findViewById(R.id.suggestions_ll);
		mContactUsLL = (LinearLayout) findViewById(R.id.contact_us_ll);

		mTitle.setText(R.string.setting);
		mBack.setOnClickListener(this);

		mHelpLL.setOnClickListener(this);
		mAppUpdateLL.setOnClickListener(this);
		mShareLL.setOnClickListener(this);
		mSuggestionsLL.setOnClickListener(this);
		mContactUsLL.setOnClickListener(this);
		mLogoutLL.setOnClickListener(this);

	}

	private void initData() {
		showHead();
		if (mUser.isIs_login()) {
			initPopularizeinfo();
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.help_ll:
			startOtherWeb(this, this.getString(R.string.help), MyURL.HELP_URL);
			break;
		case R.id.app_update_ll:
			onClickUpdate();
			break;
		case R.id.app_share_ll:
			String promotecode = null;
			if (mUser.isIs_login()) {
				promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
			}
			share(promotecode);
			break;
		case R.id.suggestions_ll:
			mUser = UserController.getInstance(this).getUserInfo();
			if (mUser.isIs_login()) {
				String url = MyURL.SUGGESTIONS_URL + mUser.getId();
				startOtherWeb(this, this.getString(R.string.suggestions), url);
			} else {
				hintLogin(this);
			}
			break;
		case R.id.contact_us_ll:
			startOtherWeb(this, this.getString(R.string.contact_us),
					MyURL.CONTACT_US_URL);
			break;

		case R.id.logout_btn:
			logout();
			showHead();
			break;

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}

	}

	/**
	 * 检查更新
	 */
	private void onClickUpdate() {
		new UpdateManager(this, false);
	}

	/**
	 * 显示用户头像
	 */
	private void showHead() {
		mUser = UserController.getInstance(this).getUserInfo();
		if (mUser.isIs_login()) {
			mLogoutLL.setVisibility(View.VISIBLE);
		} else {
			mLogoutLL.setVisibility(View.GONE);
		}
	}

	private void initPopularizeinfo() {
		String promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
		if (StringUtils.areEmpty(promotecode)) {
			NetHelper.getPromoteInfo(mUser.getId(),
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							LogUtils.logd(TAG, "initPopularizeinfo+onSuccess");
							LogUtils.logd(TAG, "mUser.getId()" + mUser.getId());
							resolvePromoteInfo(arg2);

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							LogUtils.logd(TAG, "initPopularizeinfo+onFailure");

						}
					});
		}
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
				String mPromoteCode = result.optString("promote_code");
				AndroidUtils.savePromoteCodeInfo(mUser.getId(), mPromoteCode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

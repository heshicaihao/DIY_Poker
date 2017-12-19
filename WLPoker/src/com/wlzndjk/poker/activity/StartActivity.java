package com.wlzndjk.poker.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wlzndjk.poker.MainActivity;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.base.BaseActivity;
import com.wlzndjk.poker.bean.UserBean;
import com.wlzndjk.poker.cache.JsonDao;
import com.wlzndjk.poker.common.UserController;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.net.NetHelper;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.FileUtil;
import com.wlzndjk.poker.utils.JSONUtil;
import com.wlzndjk.poker.utils.LogUtils;
import com.wlzndjk.poker.utils.SharedpreferncesUtil;

/**
 * 欢迎页
 * 
 * @author heshicaihao
 */
@SuppressWarnings("deprecation")
public class StartActivity extends BaseActivity {

	private String mCulTime = null;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

		initData();

	}

	private void initData() {
		setStyleCustom();
		mUser = UserController.getInstance(this).getUserInfo();
		if (mUser == null) {
			initUserInfo();
		}
		mCulTime = getTime();
		getUtime();
		AndroidUtils.getScreenInfo(this);
		proofLogin();
	}

	private void getUtime() {
		int screenWidth = AndroidUtils.getScreenWidth(this);
		int screenHeight = AndroidUtils.getScreenHeight(this);
		String app_type = MyConstants.ANDROID;
		NetHelper.getAdInfo(screenWidth + "", screenHeight + "", mCulTime,
				app_type, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getAdInfo+onSuccess");
						resolveGetAdInfo(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getAdInfo+onFailure");
					}
				});

	}

	private void proofLogin() {
		if (!SharedpreferncesUtil.getProofLogin(getApplicationContext())) {
			if (mUser != null) {
				if (mUser.isIs_three_login()) {
					deletePCodeInfo();
					gotoLogout();
					String type = mUser.getType();
					if ("qzone".equals(type)) {
						deleteOauth(SHARE_MEDIA.QZONE);
					} else if ("wechat".equals(type)) {
						deleteOauth(SHARE_MEDIA.WEIXIN);
					}
				}
			}
		}
	}

	private void resolveGetAdInfo(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			if ("0".equals(code)) {
				JSONObject result = JSONUtil.resolveResult(responseBody);
				String update_time = result.optString("updatetime");
				FileUtil.saveFile(update_time, MyConstants.TIME,
						MyConstants.UPDATE_TIME, MyConstants.TXT);
				if (!update_time.equals(mCulTime)) {
					deleteFile();
					DelayedJumpNext();
				} else {
					DelayedJumpNext();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 删除部分老的不需要的缓存
	 */
	private void deleteFile() {
		FileUtil.deleteAllFiles(new File(FileUtil
				.getFileRootPath(MyConstants.CATLIST)));
		FileUtil.deleteAllFiles(new File(FileUtil
				.getFileRootPath(MyConstants.HOMEAD)));
		FileUtil.deleteAllFiles(new File(FileUtil
				.getFileRootPath(MyConstants.TEMPLATE)));
		FileUtil.deleteAllFiles(new File(FileUtil
				.getFileRootPath(MyConstants.CACH_DIR)));
		FileUtil.deleteAllFiles(new File(FileUtil
				.getFileRootPath(MyConstants.CACHE_DIR)));
	}

	/**
	 * 延时跳转下一页
	 */
	private void DelayedJumpNext() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {
						if (SharedpreferncesUtil
								.getGuided(getApplicationContext())) {
							JsonDao.getMaterial();
							gotoMainActivity();
						} else {
							gotoWelcomeActivity();
						}
					}
				});
			}
		}, 2000);
	}

	private void gotoMainActivity() {
		int CurrentTabNum = 0;
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("CurrentTabNum", CurrentTabNum);
		startActivity(intent);
		StartActivity.this.finish();
	}

	private void gotoWelcomeActivity() {
		Intent intent = new Intent(StartActivity.this, WelcomeActivity.class);
		StartActivity.this.startActivity(intent);
		StartActivity.this.finish();
	}

	/**
	 * 获取时间梭
	 * 
	 * @return
	 */
	private String getTime() {
		String time = null;
		String timefilePath = FileUtil.getFilePath(MyConstants.TIME,
				MyConstants.UPDATE_TIME, MyConstants.TXT);
		boolean fileIsExists = FileUtil.fileIsExists(timefilePath);
		if (fileIsExists) {
			time = FileUtil.readFile(MyConstants.TIME, MyConstants.UPDATE_TIME,
					MyConstants.TXT);
		} else {
			time = null;
		}
		return time;
	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();

	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();

	}
	
    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(StartActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.ic_notification;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
//        Toast.makeText(StartActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }



}

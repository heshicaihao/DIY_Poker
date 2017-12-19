package com.wlzndjk.poker.common;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.jpush.android.api.JPushInterface;

import com.umeng.socialize.PlatformConfig;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.picdeal.DealTaskManager;
import com.wlzndjk.poker.picupload.UploadTaskManager;
import com.wlzndjk.poker.utils.CrashHandler;

public class MyApplication extends Application {

	// 用于存放倒计时时间
	public static Map<String, Long> TimeMap;
	public static UploadTaskManager mUploadTaskMananger;
	public static DealTaskManager mDealTaskManager;
	private static Context context;
	private static boolean mIsAtLeastGB;
	private static String PREF_NAME = "creativelocker.pref";
	private static String LAST_REFRESH_TIME = "last_refresh_time.pref";
	private static MyApplication mInstance;
	private boolean isWriteLog = false;// 是否开启错误日志־

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		initData();

	}

	public static MyApplication getInstance() {
		if (mInstance == null) {
			mInstance = new MyApplication();
		}
		return mInstance;
	}

	/**
	 * 获得当前app运行的Context
	 * 
	 * @return
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * 获得上传任务管理器
	 * 
	 * @return
	 */
	public static UploadTaskManager getUploadTaskMananger() {
		return mUploadTaskMananger;
	}

	/**
	 * 获得上传任务管理器
	 * 
	 * @return
	 */
	public static DealTaskManager getDealTaskManager() {
		return mDealTaskManager;
	}

	public void initData() {
		mInstance = this;
		if (isWriteLog) {
			// 设置编写错误日志־
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(getApplicationContext());
		}
		mUploadTaskMananger = UploadTaskManager.getInstance();
		mDealTaskManager = DealTaskManager.getInstance();
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 网络状态监听
	}

	{

		// 微信 appid appsecret
		PlatformConfig.setWeixin(MyConstants.WX_APP_ID,
				MyConstants.WX_APP_SECRET);

		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo(MyConstants.SINA_APP_KEY,
				MyConstants.SINA_APP_SECRET);

		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone(MyConstants.QQ_APP_ID, MyConstants.QQ_APP_KEY);

	}

	/***
	 * 记录列表上次刷新时间
	 * 
	 * @return void
	 * @param key
	 * @param value
	 */
	public static void putToLastRefreshTime(String key, String value) {
		SharedPreferences preferences = getPreferences(LAST_REFRESH_TIME);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		apply(editor);
	}

	@SuppressLint("NewApi")
	public static void apply(SharedPreferences.Editor editor) {
		if (mIsAtLeastGB) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	public static void set(String key, boolean value) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(key, value);
		apply(editor);
	}

	public static void set(String key, String value) {
		Editor editor = getPreferences().edit();
		editor.putString(key, value);
		apply(editor);
	}

	public static boolean get(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	public static String get(String key, String defValue) {
		return getPreferences().getString(key, defValue);
	}

	public static int get(String key, int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	public static long get(String key, long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	public static float get(String key, float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	public static SharedPreferences getPreferences() {
		SharedPreferences pre = context.getSharedPreferences(PREF_NAME,
				Context.MODE_MULTI_PROCESS);
		return pre;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	public static SharedPreferences getPreferences(String prefName) {
		return context.getSharedPreferences(prefName,
				Context.MODE_MULTI_PROCESS);
	}

	private static ArrayList<Activity> atyList = new ArrayList<Activity>();

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		atyList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		if (atyList == null) {
			return;
		}
		for (int i = 0; i < atyList.size(); i++) {
			atyList.get(i).finish();
		}
	}
}

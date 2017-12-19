package com.wlzndjk.poker.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.base.BaseActivity;
import com.wlzndjk.poker.bean.UserBean;
import com.wlzndjk.poker.common.UserController;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.dialog.CustomProgressDialog;
import com.wlzndjk.poker.net.NetHelper;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.FileUtil;
import com.wlzndjk.poker.utils.LogUtils;
import com.wlzndjk.poker.utils.StringUtils;
import com.wlzndjk.poker.widget.ScrollWebView;
import com.wlzndjk.poker.widget.ScrollWebView.ScrollInterface;

/**
 * 无头Web网页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
@SuppressLint({ "SetJavaScriptEnabled", "ClickableViewAccessibility" })
public class OtherWebNoTitleActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private ScrollWebView mWebview;
	private String mTitleStr;
	private String mUrl;
	private CustomProgressDialog mDialog;
	private UserBean mUser;
	private TextView mBtnTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_no_title_web);

		initView();
		initData();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void initData() {
		getDataIntent();
		mTitle.setText(mTitleStr);
		if (!StringUtils.isEmpty(mUrl)) {
			initWebView(mWebview, mUrl);
		}

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

	private void getDataIntent() {
		Intent intent = getIntent();
		mTitleStr = intent.getStringExtra("title");
		mUrl = intent.getStringExtra("url");
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mWebview = (ScrollWebView) findViewById(R.id.webview);
		mBtnTextView = (TextView) findViewById(R.id.btn_textview);

		mBtnTextView.setVisibility(View.GONE);
		mBack.setOnClickListener(this);
		mBtnTextView.setOnClickListener(this);
		mDialog = new CustomProgressDialog(this, this.getResources().getString(
				R.string.loading));

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		case R.id.btn_textview:
			String promotecode = null;
			if (mUser.isIs_login()) {
				promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
			}
			share(promotecode);
			break;
		default:
			break;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	public void initWebView(final WebView Wv, String url) {

		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				mDialog.dismiss();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}
		});
		// mWebview.addJavascriptInterface(new
		// JavaScriptObject(getApplication(),
		// this,mUser.getId()), "toApp");
		mWebview.loadUrl(url);
		mDialog.show();
		mWebview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& mWebview.canGoBack()) { // 表示按返回键
						mWebview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});

		mWebview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					v.requestFocusFromTouch();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				}
				return false;
			}

		});

		webViewScroolChangeListener();
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

	/**
	 * 
	 * 设置滚动监听
	 */
	private void webViewScroolChangeListener() {
		mWebview.setOnCustomScroolChangeListener(new ScrollInterface() {
			@Override
			public void onSChanged(int l, int t, int oldl, int oldt) {
				// WebView的总高度
				float webViewContentHeight = mWebview.getContentHeight()
						* mWebview.getScale();
				// WebView的现高度
				float webViewCurrentHeight = (mWebview.getHeight() + mWebview
						.getScrollY());
				if ((webViewContentHeight - webViewCurrentHeight) < 80) {
					mBtnTextView.setVisibility(View.VISIBLE);
				}else{
					mBtnTextView.setVisibility(View.GONE);
				}
			}
		});
	}

}

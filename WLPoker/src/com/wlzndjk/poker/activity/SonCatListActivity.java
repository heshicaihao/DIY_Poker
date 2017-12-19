package com.wlzndjk.poker.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.adapter.SonCatListAdapter;
import com.wlzndjk.poker.base.BaseActivity;
import com.wlzndjk.poker.common.MyApplication;
import com.wlzndjk.poker.net.NetHelper;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.LogUtils;

@SuppressWarnings("deprecation")
public class SonCatListActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private ListView mListView;
	private String mCatId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_son_cat_list);

		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
			
		default:
			break;
			
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AndroidUtils.finishActivity(this);
		}
		return false;
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mListView = (ListView) findViewById(R.id.goods_listview);

		mBack.setOnClickListener(this);

	}

	private void initData() {
		getDataIntent();
		initSonCatList();
	}

	private void initSonCatList() {
		getDataIntent();
		String mTime = " ";
		NetHelper.getSonList(mTime, mCatId, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "getCatList+onSuccess");
				resolveSonCatList(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getCatList+onFailure");
			}
		});

	}

	private void resolveSonCatList(byte[] responseBody) {

		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "mItemsjson:" + json);
			JSONObject JSONObject = new JSONObject(json);
			JSONArray mItems = JSONObject.getJSONArray("result");
			ListSetAdapter(mItems);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ListView 添加数据
	 * 
	 * @param result
	 * @throws JSONException
	 */
	private void ListSetAdapter(JSONArray items) throws JSONException {

		SonCatListAdapter mAdapter = new SonCatListAdapter(this,
				MyApplication.getContext(), items);
		mListView.setAdapter(mAdapter);
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mCatId = intent.getStringExtra("cat_id");
		String cat_name = intent.getStringExtra("cat_name");
		mTitle.setText(cat_name);

	}

}

package com.wlzndjk.poker.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wlzndjk.poker.R;
import com.wlzndjk.poker.activity.PaySuccessActivity;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.utils.AndroidUtils;
import com.wlzndjk.poker.utils.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI mApi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wx_pay_result);

		mApi = WXAPIFactory.createWXAPI(this, MyConstants.WX_APP_ID);
		mApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				gotoPaySuccess();
				this.finish();
			} else {
				ToastUtils.show(R.string.pay_failure);
				AndroidUtils.finishActivity(this);
			}
		}
	}

	private void gotoPaySuccess() {
		Intent intent = new Intent(this, PaySuccessActivity.class);
		this.startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

}
package com.wlzndjk.poker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 设置含滑动监听的 WebView
 * @author Administrator
 *
 */
public class ScrollWebView extends WebView {
	
	public ScrollInterface mScrollInterface;

	public ScrollWebView(Context context) {
		super(context);
	}

	public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);
		mScrollInterface.onSChanged(l, t, oldl, oldt);
	}

	public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {

		this.mScrollInterface = scrollInterface;

	}

	public interface ScrollInterface {

		public void onSChanged(int l, int t, int oldl, int oldt);

	}
	
}

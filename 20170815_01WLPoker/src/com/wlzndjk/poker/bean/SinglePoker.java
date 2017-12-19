package com.wlzndjk.poker.bean;

import com.wlzndjk.poker.base.BaseBean;

public class SinglePoker extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private MobileShell mobileShell;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MobileShell getMobileShell() {
		return mobileShell;
	}

	public void setMobileShell(MobileShell mobileShell) {
		this.mobileShell = mobileShell;
	}

	@Override
	public String toString() {
		return "SinglePoker [type=" + type + ", mobileShell=" + mobileShell
				+ "]";
	}

	public SinglePoker() {
		super();
	}

	public SinglePoker(String type, MobileShell mobileShell) {
		super();
		this.type = type;
		this.mobileShell = mobileShell;
	}

}

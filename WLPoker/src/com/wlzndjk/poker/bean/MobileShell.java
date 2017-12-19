package com.wlzndjk.poker.bean;

import java.util.List;

import com.wlzndjk.poker.base.BaseBean;


public class MobileShell extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<FrameBean> listframe;
	private List<DecorationBean> listDecoration;
	private List<BackgroundBean> listBackground;
	private List<PageBean> listPage;

	public List<FrameBean> getListframe() {
		return listframe;
	}

	public void setListframe(List<FrameBean> listframe) {
		this.listframe = listframe;
	}


	public List<DecorationBean> getListDecoration() {
		return listDecoration;
	}


	public void setListDecoration(List<DecorationBean> listDecoration) {
		this.listDecoration = listDecoration;
	}


	public List<BackgroundBean> getListBackground() {
		return listBackground;
	}


	public void setListBackground(List<BackgroundBean> listBackground) {
		this.listBackground = listBackground;
	}


	public List<PageBean> getListPage() {
		return listPage;
	}


	public void setListPage(List<PageBean> listPage) {
		this.listPage = listPage;
	}


	public MobileShell(List<FrameBean> listframe,
			List<DecorationBean> listDecoration,
			List<BackgroundBean> listBackground, List<PageBean> listPage) {
		super();
		this.listframe = listframe;
		this.listDecoration = listDecoration;
		this.listBackground = listBackground;
		this.listPage = listPage;
	}

	@Override
	public String toString() {
		return "MobileShell [listframe=" + listframe + ", listDecoration="
				+ listDecoration + ", listBackground=" + listBackground
				+ ", listPage=" + listPage + "]";
	}

	public MobileShell() {
		super();
	}
	

}
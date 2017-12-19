package com.wlzndjk.poker.bean;

import java.util.List;

import com.wlzndjk.poker.base.BaseBean;

/**
 * 扑克全套
 * 
 * @author heshicaihao
 * 
 */
public class PokerBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pagetype;// 总页数
	private List<MobileShell> listBigace;// 大王
	private List<MobileShell>  listSmallace;// 小王
	private List<MobileShell>  listBackup;// 空白页
	private List<MobileShell>  listInpage;// 内页
	
	public String getPagetype() {
		return pagetype;
	}


	public void setPagetype(String pagetype) {
		this.pagetype = pagetype;
	}


	public List<MobileShell> getListBigace() {
		return listBigace;
	}


	public void setListBigace(List<MobileShell> listBigace) {
		this.listBigace = listBigace;
	}


	public List<MobileShell> getListSmallace() {
		return listSmallace;
	}


	public void setListSmallace(List<MobileShell> listSmallace) {
		this.listSmallace = listSmallace;
	}


	public List<MobileShell> getListBackup() {
		return listBackup;
	}


	public void setListBackup(List<MobileShell> listBackup) {
		this.listBackup = listBackup;
	}


	public List<MobileShell> getListInpage() {
		return listInpage;
	}


	public void setListInpage(List<MobileShell> listInpage) {
		this.listInpage = listInpage;
	}

	public PokerBean(String pagetype, List<MobileShell> listBigace,
			List<MobileShell> listSmallace, List<MobileShell> listBackup,
			List<MobileShell> listInpage) {
		super();
		this.pagetype = pagetype;
		this.listBigace = listBigace;
		this.listSmallace = listSmallace;
		this.listBackup = listBackup;
		this.listInpage = listInpage;
	}


	@Override
	public String toString() {
		return "PokerBean [pagetype=" + pagetype + ", listBigace=" + listBigace
				+ ", listSmallace=" + listSmallace + ", listBackup="
				+ listBackup + ", listInpage=" + listInpage + "]";
	}

	public PokerBean() {
		super();
	}


}

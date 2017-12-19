package com.wlzndjk.poker.bean;

import com.wlzndjk.poker.base.BaseBean;

public class BackgroundBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String elementType;// 类型special,text,decoration,frame,background,photo,page,smart,canvas
	private String id;// 唯一ID
	private String x;// 元素x坐标
	private String y;// 元素y坐标
	private String width;// 元素整体宽度
	private String height;// 元素整体高度
	private String imgid;// 背景表ID
	private String url;// 背景图片地址
	private String theOrder;// 排序背景元素的排序永远为0
	private String pageID;// 当前模板页ID
	private String remark;

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getImgid() {
		return imgid;
	}

	public void setImgid(String imgid) {
		this.imgid = imgid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTheOrder() {
		return theOrder;
	}

	public void setTheOrder(String theOrder) {
		this.theOrder = theOrder;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	public BackgroundBean(String elementType, String id, String x, String y,
			String width, String height, String imgid, String url,
			String theOrder, String pageID, String remark) {
		super();
		this.elementType = elementType;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.imgid = imgid;
		this.url = url;
		this.theOrder = theOrder;
		this.pageID = pageID;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "BackgroundBean [elementType=" + elementType + ", id=" + id
				+ ", x=" + x + ", y=" + y + ", width=" + width + ", height="
				+ height + ", imgid=" + imgid + ", url=" + url + ", theOrder="
				+ theOrder + ", pageID=" + pageID + ", remark=" + remark + "]";
	}

	public BackgroundBean() {
		super();
	}
}
package com.wlzndjk.poker.bean;


import com.wlzndjk.poker.base.BaseBean;


public class PageBean extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String elementType;// 类型special,page,decoration,frame,background,photo,page,smart,canvas
	private String id;// 唯一ID
	private String diretion;// 当前模板中图片是横向或竖向（v=竖向，h=横向）
	private String pics;// 用户图片数量
	private String proType;// 产品类型：1=胶装杂志，2=锁线精装书，3=布纹书（精装），4=琉璃书，5=台历，6=照片冲印，7=照片卡
	private String pageType;// 页面类型：1=封面，2=扉页，3=内页，4=底页
	private String themeType;// 剧本类型：1=旅游，2=亲子，3=摄影，4=生活
	private String pageWidth;// 模板页宽度
	private String pageHeight;// 模板页高度
	private String bleeding;// 出血位一般为3毫米，单位：像素
	private String offsetX;// 舞台区的偏移量(app用的)
	private String offsetY;// 舞台区的偏移量(app用的)
	private String thickness;// 页面的书脊厚度
	private String pageRate;// 记录当前页的显示比例
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

	public String getDiretion() {
		return diretion;
	}

	public void setDiretion(String diretion) {
		this.diretion = diretion;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getThemeType() {
		return themeType;
	}

	public void setThemeType(String themeType) {
		this.themeType = themeType;
	}

	public String getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(String pageWidth) {
		this.pageWidth = pageWidth;
	}

	public String getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(String pageHeight) {
		this.pageHeight = pageHeight;
	}

	public String getBleeding() {
		return bleeding;
	}

	public void setBleeding(String bleeding) {
		this.bleeding = bleeding;
	}

	public String getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(String offsetX) {
		this.offsetX = offsetX;
	}

	public String getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(String offsetY) {
		this.offsetY = offsetY;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getPageRate() {
		return pageRate;
	}

	public void setPageRate(String pageRate) {
		this.pageRate = pageRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PageBean(String elementType, String id, String diretion,
			String pics, String proType, String pageType, String themeType,
			String pageWidth, String pageHeight, String bleeding,
			String offsetX, String offsetY, String thickness, String pageIndex,
			String pageRate) {
		super();
		this.elementType = elementType;
		this.id = id;
		this.diretion = diretion;
		this.pics = pics;
		this.proType = proType;
		this.pageType = pageType;
		this.themeType = themeType;
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.bleeding = bleeding;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.thickness = thickness;
		this.pageRate = pageRate;
	}

	@Override
	public String toString() {
		return "PageBean [elementType=" + elementType + ", id=" + id
				+ ", diretion=" + diretion + ", pics=" + pics + ", proType="
				+ proType + ", pageType=" + pageType + ", themeType="
				+ themeType + ", pageWidth=" + pageWidth + ", pageHeight="
				+ pageHeight + ", bleeding=" + bleeding + ", offsetX="
				+ offsetX + ", offsetY=" + offsetY + ", thickness=" + thickness
				+ ", pageRate=" + pageRate + ", remark=" + remark + "]";
	}

	public PageBean() {
		super();
	}

}

package com.ca.sport.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ad {

/*	"srcB":"/images/5653e38eNed7f4ffc.jpg",
	"height":399,
	"alt":"",
	"width":670,
	"src":"/images/5653e38eNed7f4ffc.jpg",
	"widthB":550,
	"href":"javascript:;",
	"heightB":399*/
	@JsonProperty(value = "srcB")
	private String srcC;
	private Integer height = 399;
	private String alt;
	private String width = "670";
	private String src = "/images/5653e38eNed7f4ffc.jpg";
	private String widthB = "550";
	private String href = "/";
	private String heightB = "399";



	public String getSrcC() {
		return srcC;
	}
	public void setSrcC(String srcC) {
		this.srcC = srcC;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getWidthB() {
		return widthB;
	}
	public void setWidthB(String widthB) {
		this.widthB = widthB;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getHeightB() {
		return heightB;
	}
	public void setHeightB(String heightB) {
		this.heightB = heightB;
	}




}

package com.whyq.model;

import java.util.Date;

public class MenuItem {

	private int itemid;
	private int cafeid;
	private String name;
	private String sizeable;
	private int parentid;
	private String type;
	private String imgurl;

	public MenuItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MenuItem(int itemid, int cafeid, String name, String sizeable, int parentid, String type) {
		super();
		this.itemid = itemid;
		this.cafeid = cafeid;
		this.name = name;
		this.sizeable = sizeable;
		this.parentid = parentid;
		this.type = type;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public int getCafeid() {
		return cafeid;
	}

	public void setCafeid(int cafeid) {
		this.cafeid = cafeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSizeable() {
		return sizeable;
	}

	public void setSizeable(String sizeable) {
		this.sizeable = sizeable;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	@Override
	public String toString() {
		return "MenuItem [itemid=" + itemid + ", cafeid=" + cafeid + ", name=" + name + ", sizeable=" + sizeable
				+ ", parentid=" + parentid + ", type=" + type + ", imgurl=" + imgurl + "]";
	}

}
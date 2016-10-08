package com.whyq.model;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderInformation {
	private String orderNum;
	private GupshupObject gupshupObject;
	List<CartItem> orderList = new ArrayList<CartItem>();
	private int cafeid;
	private float totalamount;
	private String payMethod;
	private String payStatus;
	private int maxnumber;

	public OrderInformation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderInformation(GupshupObject gupshupObject, List<CartItem> orderList, int cafeid) {
		super();
		this.gupshupObject = gupshupObject;
		this.orderList = orderList;
		this.cafeid = cafeid;
	}

	public OrderInformation(String orderNum, GupshupObject gupshupObject, List<CartItem> orderList, int cafeid) {
		super();
		this.orderNum = orderNum;
		this.gupshupObject = gupshupObject;
		this.orderList = orderList;
		this.cafeid = cafeid;
	}

	
	public GupshupObject getGupshupObject() {
		return gupshupObject;
	}

	public void setGupshupObject(GupshupObject gupshupObject) {
		this.gupshupObject = gupshupObject;
	}

	public List<CartItem> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<CartItem> orderList) {
		this.orderList = orderList;
	}

	public int getCafeid() {
		return cafeid;
	}

	public void setCafeid(int cafeid) {
		this.cafeid = cafeid;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public float getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(float totalamount) {
		this.totalamount = totalamount;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public int getMaxnumber() {
		return maxnumber;
	}

	public void setMaxnumber(int maxnumber) {
		this.maxnumber = maxnumber;
	}

	@Override
	public String toString() {
		return "OrderInformation [orderNum=" + orderNum + ", gupshupObject=" + gupshupObject + ", orderList="
				+ orderList + ", cafeid=" + cafeid + ", totalamount=" + totalamount + ", payMethod=" + payMethod
				+ ", payStatus=" + payStatus + ", maxnumber=" + maxnumber + "]";
	}

}

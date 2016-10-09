package com.whyq.model;

import java.util.ArrayList;

public class Receipt {

	private int cafeid;
	private String cafename;
	private String recipientName;
	private String orderNumber;
	private String currency;
	private String paymentMethod;
	private String orderUrl;
	private String timestamp;
	
	private ArrayList<ReceiptElement> receiptElementList;

	private int total_cost;

	public int getCafeid() {
		return cafeid;
	}

	public void setCafeid(int cafeid) {
		this.cafeid = cafeid;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<ReceiptElement> getReceiptElementList() {
		return receiptElementList;
	}

	public void setReceiptElementList(ArrayList<ReceiptElement> receiptElementList) {
		this.receiptElementList = receiptElementList;
	}

	public int getTotal_cost() {
		return total_cost;
	}

	public void setTotal_cost(int total_cost) {
		this.total_cost = total_cost;
	}

	public String getCafename() {
		return cafename;
	}

	public void setCafename(String cafename) {
		this.cafename = cafename;
	}

	@Override
	public String toString() {
		return "Receipt [cafeid=" + cafeid + ", cafename=" + cafename + ", recipientName=" + recipientName
				+ ", orderNumber=" + orderNumber + ", currency=" + currency + ", paymentMethod=" + paymentMethod
				+ ", orderUrl=" + orderUrl + ", timestamp=" + timestamp + ", receiptElementList=" + receiptElementList
				+ ", total_cost=" + total_cost + "]";
	}

}
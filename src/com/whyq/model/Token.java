package com.whyq.model;

public class Token {
	private int id;
	private String orderId;
	private int orderLineId;
	private String Status;

	public Token() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Token(int id, String orderId, String status) {
		super();
		this.id = id;
		this.orderId = orderId;
		Status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(int orderLineId) {
		this.orderLineId = orderLineId;
	}

	@Override
	public String toString() {
		return "Token [id=" + id + ", orderId=" + orderId + ", orderLineId=" + orderLineId + ", Status=" + Status + "]";
	}

}

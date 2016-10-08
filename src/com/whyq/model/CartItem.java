package com.whyq.model;

public class CartItem {
	private MenuItem menuItem;
	private int quantity;
	private float unitPrice;
	private float totalPrice;
	private Size size;

	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItem(MenuItem menuItem) {
		super();
		this.menuItem = menuItem;
	}

	public CartItem(MenuItem menuItem, float unitPrice) {
		super();
		this.menuItem = menuItem;
		this.unitPrice = unitPrice;
	}

	public CartItem(MenuItem menuItem, int quantity, float unitPrice, float totalPrice) {
		super();
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
	}

	public CartItem(MenuItem menuItem, int quantity, Size size) {
		super();
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.size = size;
	}
	

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public CartItem(MenuItem menuItem, int quantity, float unitPrice, float totalPrice, Size size) {
		super();
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
		this.size = size;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	
	
	@Override
	public String toString() {
		return "CartItem [menuItem=" + menuItem + ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", totalPrice=" + totalPrice + ", size=" + size + "]";
	}

}

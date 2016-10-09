package com.whyq.model;

public class ReceiptElement {

	//Item name
	private String title;
	//subtitle is token
	private String subtitle;
	private String quantity;
	private int price;
	private String currency;
	private String image_url;
	private String itemid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	@Override
	public String toString() {
		return "ReceiptElement [title=" + title + ", subtitle=" + subtitle + ", quantity=" + quantity + ", price="
				+ price + ", currency=" + currency + ", image_url=" + image_url + ", itemid=" + itemid + "]";
	}

}
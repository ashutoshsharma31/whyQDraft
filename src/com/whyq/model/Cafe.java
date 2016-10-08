package com.whyq.model;


public class Cafe {

	private int cafeid;
	private String name;
	private String address;
	private String code;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Cafe [cafeid=" + cafeid + ", name=" + name + ", address=" + address + ", code=" + code + "]";
	}

}
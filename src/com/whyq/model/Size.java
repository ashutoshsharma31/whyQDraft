package com.whyq.model;

import java.util.Date;

public class Size {

	private int sizeid;
	private String desc;
	private String status;

	public Size() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Size(int sizeid, String desc, String status) {
		super();
		this.sizeid = sizeid;
		this.desc = desc;
		this.status = status;
	}
	

	public int getSizeid() {
		return sizeid;
	}

	public void setSizeid(int sizeid) {
		this.sizeid = sizeid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		String sizeName = (String) obj;
		boolean result = false;
		if (sizeName.equals(desc)) {
			result = true;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Size [sizeid=" + sizeid + ", desc=" + desc + ", status=" + status + "]";
	}

}
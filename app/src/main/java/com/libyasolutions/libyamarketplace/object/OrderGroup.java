package com.libyasolutions.libyamarketplace.object;

import java.io.Serializable;

public class OrderGroup implements Serializable{

	private String id = "";
	private String groudCode;
	private double price = 0;
	private String isRead;
	private String datetime = ""; // yyyy-mm-dd HH:MM:SS
	private String status = "1";

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getGroudCode() {
		return groudCode;
	}

	public void setGroudCode(String groudCode) {
		this.groudCode = groudCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

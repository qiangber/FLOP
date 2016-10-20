package com.flop.model;

import java.util.Date;

public class Notification {
	
	public static final String ORDER_CLIENT = "0";
	
	public static final String ORDER_SERVER = "1"; 
	
	private int id;
	private Date date;
	private String type;
	private String hasRead;
	private String userId;
	private Order order; 
	
	public Notification() {};
	
	public Notification(Date date, String type, String hasRead, String userId, Order order) {
		this.date = date;
		this.type = type;
		this.hasRead = hasRead;
		this.userId = userId;
		this.order = order;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHasRead() {
		return hasRead;
	}
	public void setHasRead(String hasRead) {
		this.hasRead = hasRead;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}	
}

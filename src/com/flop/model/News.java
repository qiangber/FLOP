package com.flop.model;

public class News {
	
	private int id;
	private String title;
	private String content;
	private String date; 
	
	public News() {
		
	}
	
	public News(String title, String content, String date) {
		super();
		this.title = title;
		this.content = content;
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}	
}

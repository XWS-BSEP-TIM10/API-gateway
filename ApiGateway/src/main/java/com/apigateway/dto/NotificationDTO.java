package com.apigateway.dto;

import java.util.Date;

public class NotificationDTO {
	private String id;
	private Boolean read;
	private String text;
	private Date timestamp;
	
	public NotificationDTO() {
		super();
	}

	public NotificationDTO(String id, Boolean read, String text, Date timestamp) {
		super();
		this.id = id;
		this.read = read;
		this.text = text;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public Boolean getRead() {
		return read;
	}

	public String getText() {
		return text;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	
	
}

package com.apigateway.dto;

public class ChatNotificationDTO {
	 private String id;
	 private String senderId;
	 private String senderName;
	 
	public ChatNotificationDTO() {
		super();
	}

	public ChatNotificationDTO(String id, String senderId, String senderName) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.senderName = senderName;
	}

	public String getId() {
		return id;
	}

	public String getSenderId() {
		return senderId;
	}

	public String getSenderName() {
		return senderName;
	}
	 
	
	 
}

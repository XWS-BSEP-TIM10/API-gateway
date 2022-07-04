package com.apigateway.dto;

import java.util.Date;

public class ChatMessageDTO {
	   private String id;
	   private String chatId;
	   private String senderId;
	   private String recipientId;
	   private String senderName;
	   private String recipientName;
	   private String content;
	   private Date timestamp;
	   private String status;
	
	   
	public ChatMessageDTO() {
		super();
	}

	public ChatMessageDTO(String id, String chatId, String senderId, String recipientId, String senderName, String recipientName,
			String content, Date timestamp, String status) {
		super();
		this.id = id;
		this.chatId = chatId;
		this.senderId = senderId;
		this.recipientId = recipientId;
		this.senderName = senderName;
		this.recipientName = recipientName;
		this.content = content;
		this.timestamp = timestamp;
		this.status = status;
	}
		
	public String getChatId() {
		return chatId;
	}
	public String getSenderId() {
		return senderId;
	}
	public String getRecipientId() {
		return recipientId;
	}
	public String getSenderName() {
		return senderName;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public String getContent() {
		return content;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public String getStatus() {
		return status;
	}

	public String getId() {
		return id;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	   
	   
}

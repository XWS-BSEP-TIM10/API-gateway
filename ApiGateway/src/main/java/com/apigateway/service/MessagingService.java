package com.apigateway.service;

import com.apigateway.dto.ChatMessageDTO;

import proto.ChatMessageResponseProto;
import proto.FindChatMessagesResponseProto;

public interface MessagingService {
	
	ChatMessageResponseProto add(ChatMessageDTO dto);
	FindChatMessagesResponseProto findChatMessages(String senderId, String recipientId);

}

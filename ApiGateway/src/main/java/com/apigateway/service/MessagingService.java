package com.apigateway.service;

import com.apigateway.dto.ChatMessageDTO;

import proto.ChatMessageResponseProto;

public interface MessagingService {
	
	ChatMessageResponseProto add(ChatMessageDTO dto);

}

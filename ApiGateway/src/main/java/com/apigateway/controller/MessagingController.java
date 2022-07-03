package com.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.apigateway.dto.ChatMessageDTO;
import com.apigateway.dto.ChatNotificationDTO;

import proto.ChatMessageResponseProto;
import com.apigateway.service.MessagingService;

@Controller
public class MessagingController {
	
    private SimpMessagingTemplate messagingTemplate;
	private final MessagingService messagingService;
	
	@Autowired
	public MessagingController(MessagingService messagingService, SimpMessagingTemplate messagingTemplate) {
		this.messagingService =messagingService;
		this.messagingTemplate = messagingTemplate;
	}
	
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
    	ChatMessageResponseProto responseProto = messagingService.add(chatMessage);
        
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),"/queue/messages",
                new ChatNotificationDTO(
                		responseProto.getId(),
                		responseProto.getSenderId(),
                		responseProto.getSenderName()));
    }

}

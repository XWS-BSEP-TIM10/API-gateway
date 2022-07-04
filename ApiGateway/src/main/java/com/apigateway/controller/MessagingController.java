package com.apigateway.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apigateway.dto.ChatMessageDTO;
import com.apigateway.dto.ChatNotificationDTO;

import proto.ChatMessageResponseProto;
import proto.FindChatMessageProto;
import proto.FindChatMessagesResponseProto;
import proto.UserNamesResponseProto;

import com.apigateway.service.MessagingService;
import com.apigateway.service.UserService;

@Controller
public class MessagingController {
	
    private SimpMessagingTemplate messagingTemplate;
	private final MessagingService messagingService;
	private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private final UserService userService;
	
	@Autowired
	public MessagingController(MessagingService messagingService, SimpMessagingTemplate messagingTemplate, UserService userService) {
		this.messagingService =messagingService;
		this.messagingTemplate = messagingTemplate;
		this.userService = userService;
	}
	
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
    	UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(chatMessage.getSenderId());
    	chatMessage.setSenderName(userNamesResponseProto.getFirstName()+ " "+ userNamesResponseProto.getLastName());
    	ChatMessageResponseProto responseProto = messagingService.add(chatMessage);
        
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),"/queue/messages",
                new ChatNotificationDTO(
                		responseProto.getId(),
                		responseProto.getSenderId(),
                		responseProto.getSenderName()));
    }
    
    @PreAuthorize("hasAuthority('UPDATE_PROFILE_PERMISSION')")
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageDTO>> findChatMessages ( @PathVariable String senderId, @PathVariable String recipientId) {
    	
    	FindChatMessagesResponseProto responseProto = messagingService.findChatMessages(senderId, recipientId);
    	List<ChatMessageDTO> messages = new ArrayList<ChatMessageDTO>();
    	for(FindChatMessageProto proto: responseProto.getMessagesList()) {
    		try {
				ChatMessageDTO dto = new ChatMessageDTO(proto.getId(),proto.getChatId(),proto.getSenderId(),proto.getRecipientId(),proto.getSenderName(),proto.getRecipientName(),proto.getContent(),iso8601Formatter.parse(proto.getTimestamp()),proto.getStatus());
				messages.add(dto);
    		} catch (ParseException e) {
    			return ResponseEntity.internalServerError().build();
			}
    	}
        return ResponseEntity
                .ok(messages);
    }

}

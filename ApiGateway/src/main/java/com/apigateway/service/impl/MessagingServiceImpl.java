package com.apigateway.service.impl;

import java.text.SimpleDateFormat;

import com.apigateway.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apigateway.dto.ChatMessageDTO;
import com.apigateway.service.MessagingService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.*;

@Service
public class MessagingServiceImpl implements MessagingService{
	
	private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private final NotificationService notificationService;
	
	@GrpcClient("messaginggrpcservice")
	private MessagingGrpcServiceGrpc.MessagingGrpcServiceBlockingStub stub;
	
	@Autowired
	public MessagingServiceImpl(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	public ChatMessageResponseProto add(ChatMessageDTO dto) {
		ChatMessageProto chatMessageProto = ChatMessageProto.newBuilder().setSenderId(dto.getSenderId())
				.setRecipientId(dto.getRecipientId()).setSenderName(dto.getSenderName())
				.setRecipientName(dto.getRecipientName()).setContent(dto.getContent())
				.setTimestamp(iso8601Formatter.format(dto.getTimestamp())).build();

		NewNotificationProto newNotificationProto = NewNotificationProto.newBuilder().setUserId(dto.getRecipientId())
				.setText("New message from "+ dto.getSenderName() + "!").build();
		notificationService.add(newNotificationProto);
		return this.stub.add(chatMessageProto);
	}
	
	@Override
	public FindChatMessagesResponseProto findChatMessages(String senderId, String recipientId) {
		FindChatMessagesProto findChatMessagesProto = FindChatMessagesProto.newBuilder().setRecipientId(recipientId).setSenderId(senderId).build();
		return this.stub.findChatMessages(findChatMessagesProto);
	}

	@Override
	public MessagingEventResponseProto getEvents() {
		MessagingEventProto eventProto = MessagingEventProto.newBuilder().build();
		return stub.getMessagingEvents(eventProto);
	}
}

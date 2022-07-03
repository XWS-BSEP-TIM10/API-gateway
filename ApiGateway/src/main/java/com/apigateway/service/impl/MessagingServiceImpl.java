package com.apigateway.service.impl;

import java.text.SimpleDateFormat;

import com.apigateway.dto.ChatMessageDTO;
import com.apigateway.service.MessagingService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.ChatMessageProto;
import proto.ChatMessageResponseProto;
import proto.MessagingGrpcServiceGrpc;

public class MessagingServiceImpl implements MessagingService{
	
	 private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@GrpcClient("messaginggrpcservice")
	private MessagingGrpcServiceGrpc.MessagingGrpcServiceBlockingStub stub;
	@Override
	public ChatMessageResponseProto add(ChatMessageDTO dto) {
		ChatMessageProto chatMessageProto = ChatMessageProto.newBuilder().setChatId(dto.getChatId()).setSenderId(dto.getSenderId()).setRecipientId(dto.getRecipientId()).setSenderName(dto.getSenderName()).setRecipientName(dto.getRecipientName()).setContent(dto.getContent()).setTimestamp(iso8601Formatter.format(dto.getTimestamp())).setStatus(dto.getStatus()).build();
		return this.stub.add(chatMessageProto);
	}
}

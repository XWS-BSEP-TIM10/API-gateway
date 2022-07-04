package com.apigateway.service.impl;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.apigateway.dto.ChatMessageDTO;
import com.apigateway.service.MessagingService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.ChatMessageProto;
import proto.ChatMessageResponseProto;
import proto.MessagingGrpcServiceGrpc;

@Service
public class MessagingServiceImpl implements MessagingService{
	
	 private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@GrpcClient("messaginggrpcservice")
	private MessagingGrpcServiceGrpc.MessagingGrpcServiceBlockingStub stub;
	@Override
	public ChatMessageResponseProto add(ChatMessageDTO dto) {
		ChatMessageProto chatMessageProto = ChatMessageProto.newBuilder().setSenderId(dto.getSenderId()).setRecipientId(dto.getRecipientId()).setSenderName(dto.getSenderName()).setRecipientName(dto.getRecipientName()).setContent(dto.getContent()).setTimestamp(iso8601Formatter.format(dto.getTimestamp())).build();
		return this.stub.add(chatMessageProto);
	}
}

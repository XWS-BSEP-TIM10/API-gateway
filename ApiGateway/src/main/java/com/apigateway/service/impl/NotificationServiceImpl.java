package com.apigateway.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apigateway.service.NotificationService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.GetNotificationProto;
import proto.NewNotificationProto;
import proto.NewPostNotificationProto;
import proto.NotificationGrpcServiceGrpc;
import proto.NotificationResponseProto;
import proto.NotificationsProto;

@Service
public class NotificationServiceImpl implements NotificationService {


    @GrpcClient("profilegrpcservice")
    private NotificationGrpcServiceGrpc.NotificationGrpcServiceBlockingStub stub;

    @Override
    public NotificationResponseProto add(NewNotificationProto newNotificationProto) {
        return this.stub.add(newNotificationProto);
    }

    @Override
    public NotificationsProto getNotifications(String userId) {
    	GetNotificationProto getNotificationProto = GetNotificationProto.newBuilder().setUserId(userId).build();
        return this.stub.getNotifications(getNotificationProto);
    }

	@Override
	public NotificationResponseProto changeNotificationsStatus(String userId) {
		GetNotificationProto getNotificationProto = GetNotificationProto.newBuilder().setUserId(userId).build();
		return this.stub.changeNotificationsStatus(getNotificationProto);
	}
	
	@Override
    public NotificationResponseProto addPostNotification(List<String> usersId, String postingPersonFullName) {
		NewPostNotificationProto newPostNotificationProto = NewPostNotificationProto.newBuilder().addAllUserId(usersId).setText(postingPersonFullName+" has created a new post.").build();
        return this.stub.addPostNotification(newPostNotificationProto);
    }
}

package com.apigateway.service.impl;

import com.apigateway.service.NotificationService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.*;

import java.text.SimpleDateFormat;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @GrpcClient("profilegrpcservice")
    private NotificationGrpcServiceGrpc.NotificationGrpcServiceBlockingStub stub;

    @Override
    public NotificationResponseProto add(NewNotificationProto newNotificationProto) {
        return this.stub.add(newNotificationProto);
    }

    @Override
    public NotificationsProto getNotifications(GetNotificationProto getNotificationProto) {
        return null;
    }
}

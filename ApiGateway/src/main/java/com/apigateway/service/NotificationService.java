package com.apigateway.service;

import java.util.List;

import proto.NewNotificationProto;
import proto.NotificationResponseProto;
import proto.NotificationsProto;

public interface NotificationService {

    NotificationResponseProto add(NewNotificationProto newNotificationProto);
    NotificationsProto getNotifications(String userId);
    NotificationResponseProto changeNotificationsStatus (String userId);
    NotificationResponseProto addPostNotification(List<String> usersId, String postingPersonFullName);
}


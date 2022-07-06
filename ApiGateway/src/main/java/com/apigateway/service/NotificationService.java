package com.apigateway.service;

import proto.GetNotificationProto;
import proto.NewNotificationProto;
import proto.NotificationResponseProto;
import proto.NotificationsProto;

public interface NotificationService {

    NotificationResponseProto add(NewNotificationProto newNotificationProto);
    NotificationsProto getNotifications (GetNotificationProto getNotificationProto);
}


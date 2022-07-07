package com.apigateway.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apigateway.dto.NotificationDTO;
import com.apigateway.service.NotificationService;

import proto.NotificationProto;
import proto.NotificationResponseProto;
import proto.NotificationsProto;

@RestController
@RequestMapping(value = "/api/v1")
public class NotificationController {
	private final NotificationService notificationService;
	private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	   @PreAuthorize("hasAuthority('UPDATE_PROFILE_PERMISSION')")
	    @GetMapping("/notifications/{userId}")
	    public ResponseEntity<List<NotificationDTO>> getNotificationsForUser ( @PathVariable String userId) {
		   NotificationsProto notificationsProto = notificationService.getNotifications(userId);
		   List<NotificationDTO> notifcations = new ArrayList<NotificationDTO>();
	    	for(NotificationProto proto: notificationsProto.getNotificationsList()) {
	    		try {
					notifcations.add(new NotificationDTO(proto.getId(), proto.getRead(), proto.getText(), iso8601Formatter.parse(proto.getCreationTime())));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	        return ResponseEntity.ok(notifcations);
	    }
	   
	   @PreAuthorize("hasAuthority('UPDATE_PROFILE_PERMISSION')")
	    @GetMapping("/notifications/changeStatus/{userId}")
	    public ResponseEntity<HttpStatus> changeNotificationsStatus( @PathVariable String userId) {
		   NotificationResponseProto notificationResponseProto = notificationService.changeNotificationsStatus(userId);
	    	if(notificationResponseProto.getStatus().equals("200"))
	        return ResponseEntity.ok().build();
	    	return ResponseEntity.badRequest().build();
	    }
	   
	   
}

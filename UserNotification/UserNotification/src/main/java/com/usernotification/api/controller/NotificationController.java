package com.usernotification.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usernotification.api.payload.request.NotificationRequest;
import com.usernotification.api.payload.respose.NotificationResponse;
import com.usernotification.api.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	@Autowired
	private NotificationService service;

	@PostMapping("/send")
	public NotificationResponse send(@RequestBody NotificationRequest request) {
		return service.sendNotification(request);
	}
}

package com.usernotification.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usernotification.api.enums.NotificationStatus;
import com.usernotification.api.enums.NotificationType;
import com.usernotification.api.model.Notification;
import com.usernotification.api.repository.NotificationRepository;

@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private NotificationRepository repo;

	@GetMapping("/user/{userId}")
	public List<Notification> byUser(@PathVariable Long userId) {
		return repo.findByUserId(userId);
	}

	@GetMapping("/status/{status}")
	public List<Notification> byStatus(@PathVariable NotificationStatus status) {
		return repo.findByStatus(status);
	}

	@GetMapping("/type/{type}")
	public List<Notification> byType(@PathVariable NotificationType type) {
		return repo.findByType(type);
	}
}

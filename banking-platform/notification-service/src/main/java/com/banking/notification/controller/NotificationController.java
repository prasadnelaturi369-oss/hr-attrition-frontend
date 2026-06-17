package com.banking.notification.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Service", description = "APIs for sending notifications and health checks")
public class NotificationController {

	@Operation(summary = "Send a notification", description = "Sends an email/SMS notification")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
	@PostMapping("/send")
	public Map<String, String> sendNotification(@RequestBody Map<String, String> notification) {
		System.out.println("Sending notification: " + notification);

		Map<String, String> response = new HashMap<>();
		response.put("status", "sent");
		response.put("message", "Notification sent successfully");

		return response;
	}

	@Operation(summary = "Health check", description = "Returns the health status of the notification service")
	@GetMapping("/health")
	public Map<String, String> health() {
		Map<String, String> health = new HashMap<>();
		health.put("status", "UP");
		health.put("service", "notification-service");
		return health;
	}
}
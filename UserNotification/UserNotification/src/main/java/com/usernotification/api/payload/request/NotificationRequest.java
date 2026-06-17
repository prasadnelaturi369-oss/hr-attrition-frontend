package com.usernotification.api.payload.request;

import com.usernotification.api.enums.NotificationType;

public class NotificationRequest {
	private Long userId;
	private String message;
	private NotificationType type;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

}

package com.usernotification.api.service;

import com.usernotification.api.payload.request.NotificationRequest;
import com.usernotification.api.payload.respose.NotificationResponse;

public interface NotificationService {

	NotificationResponse sendNotification(NotificationRequest request);

}

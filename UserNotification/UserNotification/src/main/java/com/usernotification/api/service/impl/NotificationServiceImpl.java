package com.usernotification.api.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usernotification.api.enums.NotificationStatus;
import com.usernotification.api.model.Notification;
import com.usernotification.api.model.NotificationPreference;
import com.usernotification.api.payload.request.NotificationRequest;
import com.usernotification.api.payload.respose.NotificationResponse;
import com.usernotification.api.repository.NotificationRepository;
import com.usernotification.api.repository.PreferenceRepository;
import com.usernotification.api.repository.UserRepository;
import com.usernotification.api.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PreferenceRepository prefRepo;

	@Autowired
	private NotificationRepository notifRepo;

	@Override
	public NotificationResponse sendNotification(NotificationRequest request) {

		userRepo.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		NotificationPreference pref = prefRepo.findByUserId(request.getUserId()).orElseGet(() -> {
			NotificationPreference newPref = new NotificationPreference();
			newPref.setUserId(request.getUserId());
			newPref.setEmailEnabled(false);
			newPref.setSmsEnabled(false);
			newPref.setPushEnabled(false);
			return prefRepo.save(newPref);
		});

		boolean enabled = switch (request.getType()) {
		case EMAIL -> pref.isEmailEnabled();
		case SMS -> pref.isSmsEnabled();
		case PUSH -> pref.isPushEnabled();
		};

		Notification notif = new Notification();
		notif.setUserId(request.getUserId());
		notif.setMessage(request.getMessage());
		notif.setType(request.getType());
		notif.setCreatedAt(LocalDateTime.now());

		if (enabled) {
			notif.setStatus(NotificationStatus.SENT);
		} else {
			notif.setStatus(NotificationStatus.FAILED);
		}

		Notification saved = notifRepo.save(notif);

		NotificationResponse res = new NotificationResponse();
		res.setId(saved.getId());
		res.setUserId(saved.getUserId());
		res.setMessage(saved.getMessage());
		res.setType(saved.getType());
		res.setStatus(saved.getStatus());
		res.setCreatedAt(saved.getCreatedAt());

		return res;
	}
}

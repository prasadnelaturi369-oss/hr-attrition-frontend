package com.usernotification.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usernotification.api.model.NotificationPreference;
import com.usernotification.api.repository.PreferenceRepository;
import com.usernotification.api.service.PreferenceService;

@Service
public class PreferenceServiceImpl implements PreferenceService {

	@Autowired
	private PreferenceRepository repo;

	@Override
	public NotificationPreference updatePreference(Long userId, NotificationPreference pref) {

		NotificationPreference existing = repo.findByUserId(userId).orElse(new NotificationPreference());

		existing.setUserId(userId);
		existing.setEmailEnabled(pref.isEmailEnabled());
		existing.setSmsEnabled(pref.isSmsEnabled());
		existing.setPushEnabled(pref.isPushEnabled());

		return repo.save(existing);
	}

	@Override
	public NotificationPreference getPreference(Long userId) {

		return repo.findByUserId(userId).orElseGet(() -> {
			NotificationPreference pref = new NotificationPreference();
			pref.setUserId(userId);
			pref.setEmailEnabled(false);
			pref.setSmsEnabled(false);
			pref.setPushEnabled(false);
			return repo.save(pref);
		});
	}
}
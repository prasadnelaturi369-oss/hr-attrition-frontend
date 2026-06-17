package com.usernotification.api.service;

import com.usernotification.api.model.NotificationPreference;

public interface PreferenceService {
	NotificationPreference updatePreference(Long userId, NotificationPreference pref);

	NotificationPreference getPreference(Long userId);
}

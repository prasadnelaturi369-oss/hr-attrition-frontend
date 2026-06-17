package com.usernotification.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usernotification.api.model.NotificationPreference;
import com.usernotification.api.service.PreferenceService;

@RestController
@RequestMapping("/preferences")
public class PreferenceController {

	@Autowired
	private PreferenceService service;

	@PutMapping("/{userId}")
	public NotificationPreference update(@PathVariable Long userId, @RequestBody NotificationPreference pref) {
		return service.updatePreference(userId, pref);
	}

	@GetMapping("/{userId}")
	public NotificationPreference get(@PathVariable Long userId) {
		return service.getPreference(userId);
	}
}

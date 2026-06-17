package com.usernotification.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usernotification.api.model.NotificationPreference;

public interface PreferenceRepository extends JpaRepository<NotificationPreference, Long> {
	Optional<NotificationPreference> findByUserId(Long userId);
}

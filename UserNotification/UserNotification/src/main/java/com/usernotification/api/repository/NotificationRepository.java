package com.usernotification.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usernotification.api.enums.NotificationStatus;
import com.usernotification.api.enums.NotificationType;
import com.usernotification.api.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUserId(Long userId);

	List<Notification> findByStatus(NotificationStatus status);

	List<Notification> findByType(NotificationType type);
}
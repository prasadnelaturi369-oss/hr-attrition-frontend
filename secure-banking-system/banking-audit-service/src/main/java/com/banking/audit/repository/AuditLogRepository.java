package com.banking.audit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.audit.entity.AuditLog;
import com.banking.audit.enums.ActionStatus;
import com.banking.audit.enums.EventType;
import com.banking.audit.enums.ResourceType;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

	List<AuditLog> findByUserIdOrderByCreatedAtDesc(UUID userId);

	List<AuditLog> findByEventTypeOrderByCreatedAtDesc(EventType eventType);

	List<AuditLog> findByResourceTypeAndResourceIdOrderByCreatedAtDesc(ResourceType resourceType, String resourceId);

	Page<AuditLog> findByUserId(UUID userId, Pageable pageable);

	Page<AuditLog> findByEventType(EventType eventType, Pageable pageable);

	Page<AuditLog> findByStatus(ActionStatus status, Pageable pageable);

	@Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate")
	List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Query("SELECT a FROM AuditLog a WHERE a.userId = :userId AND a.eventType = :eventType")
	List<AuditLog> findByUserAndEventType(@Param("userId") UUID userId, @Param("eventType") EventType eventType);

	long countByEventType(EventType eventType);

	long countByStatus(ActionStatus status);

	@Query("SELECT a.eventType, COUNT(a) FROM AuditLog a GROUP BY a.eventType")
	List<Object[]> countByEventTypeGrouped();

	@Query("DELETE FROM AuditLog a WHERE a.createdAt < :date")
	void deleteOldLogs(@Param("date") LocalDateTime date);
}
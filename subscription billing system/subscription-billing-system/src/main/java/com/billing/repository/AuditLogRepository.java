package com.billing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

	Page<AuditLog> findByAction(String action, Pageable pageable);
}
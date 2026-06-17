package com.banking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.entity.AuditLog;
import com.banking.repository.AuditLogRepository;

@Service
public class AuditService {

	private static final Logger log = LoggerFactory.getLogger(AuditService.class);
	private final AuditLogRepository auditLogRepository;

	public AuditService(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	public void log(String action, String entityType, String entityId, String details) {
		AuditLog auditLog = new AuditLog();
		auditLog.setAction(action);
		auditLog.setEntityType(entityType);
		auditLog.setEntityId(entityId);
		auditLog.setDetails(details);

		auditLogRepository.save(auditLog);
		log.info("AUDIT: {} - {} - {}", action, entityType, details);
	}
}
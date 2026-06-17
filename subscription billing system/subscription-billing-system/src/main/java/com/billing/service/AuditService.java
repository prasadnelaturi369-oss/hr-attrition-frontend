package com.billing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.billing.entity.AuditLog;
import com.billing.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

	private static final Logger log = LoggerFactory.getLogger(AuditService.class);

	private final AuditLogRepository auditLogRepository;

	public AuditService(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	public void log(String entityType, Long entityId, String action, String oldValue, String newValue, String remarks) {
		AuditLog auditLog = new AuditLog();
		auditLog.setEntityType(entityType);
		auditLog.setEntityId(entityId);
		auditLog.setAction(action);
		auditLog.setOldValue(oldValue);
		auditLog.setNewValue(newValue);
		auditLog.setPerformedBy("SYSTEM");
		auditLog.setIpAddress("0.0.0.0");

		auditLogRepository.save(auditLog);
		log.info("Audit log created: {} - {} on {}:{}", action, entityType, entityType, entityId);
	}
}
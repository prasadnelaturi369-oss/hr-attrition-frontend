package com.banking.audit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.audit.entity.AuditLog;
import com.banking.audit.enums.ActionStatus;
import com.banking.audit.enums.EventType;
import com.banking.audit.enums.ResourceType;
import com.banking.audit.payload.request.AuditFilterRequest;
import com.banking.audit.payload.request.AuditLogRequest;
import com.banking.audit.payload.response.AuditLogResponse;
import com.banking.audit.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuditService {

	private static final Logger log = LoggerFactory.getLogger(AuditService.class);

	@Autowired
	private AuditLogRepository auditLogRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${audit.retention.days:90}")
	private int retentionDays;

	@Transactional
	public void saveAuditLog(AuditLogRequest request) {
		log.debug("Saving audit log: {}", request);

		AuditLog auditLog = new AuditLog();
		auditLog.setEventType(request.getEventType());
		auditLog.setUserId(request.getUserId());
		auditLog.setUsername(request.getUsername());
		auditLog.setResourceType(request.getResourceType());
		auditLog.setResourceId(request.getResourceId());
		auditLog.setAction(request.getAction());
		auditLog.setOldValue(truncateValue(request.getOldValue()));
		auditLog.setNewValue(truncateValue(request.getNewValue()));
		auditLog.setStatus(request.getStatus());
		auditLog.setErrorMessage(request.getErrorMessage());
		auditLog.setIpAddress(request.getIpAddress());
		auditLog.setUserAgent(request.getUserAgent());
		auditLog.setRequestUrl(request.getRequestUrl());
		auditLog.setHttpMethod(request.getHttpMethod());
		auditLog.setExecutionTimeMs(request.getExecutionTimeMs());
		auditLog.setServiceName(request.getServiceName());

		auditLogRepository.save(auditLog);
		log.info("Audit log saved successfully. ID: {}", auditLog.getId());
	}

	public List<AuditLogResponse> getAuditLogsByUser(UUID userId) {
		log.debug("Fetching audit logs for user: {}", userId);
		return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(AuditLogResponse::fromEntity)
				.collect(Collectors.toList());
	}

	public List<AuditLogResponse> getAuditLogsByEventType(EventType eventType) {
		log.debug("Fetching audit logs for event type: {}", eventType);
		return auditLogRepository.findByEventTypeOrderByCreatedAtDesc(eventType).stream()
				.map(AuditLogResponse::fromEntity).collect(Collectors.toList());
	}

	public List<AuditLogResponse> getAuditLogsByResource(ResourceType resourceType, String resourceId) {
		log.debug("Fetching audit logs for resource: {} - {}", resourceType, resourceId);
		return auditLogRepository.findByResourceTypeAndResourceIdOrderByCreatedAtDesc(resourceType, resourceId).stream()
				.map(AuditLogResponse::fromEntity).collect(Collectors.toList());
	}

	public Page<AuditLogResponse> filterAuditLogs(AuditFilterRequest filter) {
		log.debug("Filtering audit logs with criteria: {}", filter);

		Sort.Direction direction = Sort.Direction
				.fromString(filter.getSortDirection().equalsIgnoreCase("DESC") ? "DESC" : "ASC");
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(direction, filter.getSortBy()));

		Page<AuditLog> auditLogPage = null;

		if (filter.getUserId() != null) {
			auditLogPage = auditLogRepository.findByUserId(filter.getUserId(), pageable);
		} else if (filter.getEventType() != null) {
			auditLogPage = auditLogRepository.findByEventType(filter.getEventType(), pageable);
		} else if (filter.getStatus() != null) {
			auditLogPage = auditLogRepository.findByStatus(filter.getStatus(), pageable);
		} else {
			auditLogPage = auditLogRepository.findAll(pageable);
		}

		return auditLogPage.map(AuditLogResponse::fromEntity);
	}

	@Transactional
	public void cleanupOldAuditLogs() {
		LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
		log.info("Cleaning up audit logs older than: {}", cutoffDate);
		auditLogRepository.deleteOldLogs(cutoffDate);
	}

	public long getAuditLogCountByEventType(EventType eventType) {
		return auditLogRepository.countByEventType(eventType);
	}

	public long getAuditLogCountByStatus(ActionStatus status) {
		return auditLogRepository.countByStatus(status);
	}

	private String truncateValue(String value) {
		if (value == null)
			return null;
		if (value.toLowerCase().contains("password") || value.toLowerCase().contains("token")) {
			return "[REDACTED]";
		}
		if (value.length() > 1000) {
			return value.substring(0, 1000) + "...";
		}
		return value;
	}
}
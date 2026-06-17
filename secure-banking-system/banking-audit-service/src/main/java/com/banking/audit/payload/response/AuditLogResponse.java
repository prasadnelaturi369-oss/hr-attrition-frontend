package com.banking.audit.payload.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.banking.audit.entity.AuditLog;
import com.banking.audit.enums.ActionStatus;
import com.banking.audit.enums.EventType;
import com.banking.audit.enums.ResourceType;

public class AuditLogResponse {
	private UUID id;
	private EventType eventType;
	private UUID userId;
	private String username;
	private ResourceType resourceType;
	private String resourceId;
	private String action;
	private String oldValue;
	private String newValue;
	private ActionStatus status;
	private String errorMessage;
	private String ipAddress;
	private LocalDateTime createdAt;
	private Long executionTimeMs;
	private String serviceName;

	public static AuditLogResponse fromEntity(AuditLog auditLog) {
		AuditLogResponse response = new AuditLogResponse();
		response.setId(auditLog.getId());
		response.setEventType(auditLog.getEventType());
		response.setUserId(auditLog.getUserId());
		response.setUsername(auditLog.getUsername());
		response.setResourceType(auditLog.getResourceType());
		response.setResourceId(auditLog.getResourceId());
		response.setAction(auditLog.getAction());
		response.setOldValue(auditLog.getOldValue());
		response.setNewValue(auditLog.getNewValue());
		response.setStatus(auditLog.getStatus());
		response.setErrorMessage(auditLog.getErrorMessage());
		response.setIpAddress(auditLog.getIpAddress());
		response.setCreatedAt(auditLog.getCreatedAt());
		response.setExecutionTimeMs(auditLog.getExecutionTimeMs());
		response.setServiceName(auditLog.getServiceName());
		return response;
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public ActionStatus getStatus() {
		return status;
	}

	public void setStatus(ActionStatus status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getExecutionTimeMs() {
		return executionTimeMs;
	}

	public void setExecutionTimeMs(Long executionTimeMs) {
		this.executionTimeMs = executionTimeMs;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
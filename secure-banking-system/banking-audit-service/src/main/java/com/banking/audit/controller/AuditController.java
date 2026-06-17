package com.banking.audit.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.audit.enums.ActionStatus;
import com.banking.audit.enums.EventType;
import com.banking.audit.enums.ResourceType;
import com.banking.audit.payload.request.AuditFilterRequest;
import com.banking.audit.payload.request.AuditLogRequest;
import com.banking.audit.payload.response.AuditLogResponse;
import com.banking.audit.service.AuditService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

	private static final Logger log = LoggerFactory.getLogger(AuditController.class);

	@Autowired
	private AuditService auditService;

	@PostMapping("/log")
	public ResponseEntity<Void> createAuditLog(@Valid @RequestBody AuditLogRequest request) {
		log.info("REST request to create audit log");
		auditService.saveAuditLog(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<AuditLogResponse>> getAuditLogsByUser(@PathVariable UUID userId) {
		log.info("REST request to get audit logs for user: {}", userId);
		List<AuditLogResponse> logs = auditService.getAuditLogsByUser(userId);
		return ResponseEntity.ok(logs);
	}

	@GetMapping("/event/{eventType}")
	public ResponseEntity<List<AuditLogResponse>> getAuditLogsByEventType(@PathVariable EventType eventType) {
		log.info("REST request to get audit logs for event: {}", eventType);
		List<AuditLogResponse> logs = auditService.getAuditLogsByEventType(eventType);
		return ResponseEntity.ok(logs);
	}

	@GetMapping("/resource")
	public ResponseEntity<List<AuditLogResponse>> getAuditLogsByResource(@RequestParam ResourceType resourceType,
			@RequestParam String resourceId) {
		log.info("REST request to get audit logs for resource: {} - {}", resourceType, resourceId);
		List<AuditLogResponse> logs = auditService.getAuditLogsByResource(resourceType, resourceId);
		return ResponseEntity.ok(logs);
	}

	@PostMapping("/filter")
	public ResponseEntity<Page<AuditLogResponse>> filterAuditLogs(@RequestBody AuditFilterRequest filter) {
		log.info("REST request to filter audit logs");
		Page<AuditLogResponse> logs = auditService.filterAuditLogs(filter);
		return ResponseEntity.ok(logs);
	}

	@DeleteMapping("/cleanup")
	public ResponseEntity<Void> cleanupOldLogs() {
		log.info("REST request to cleanup old audit logs");
		auditService.cleanupOldAuditLogs();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/stats/event/{eventType}")
	public ResponseEntity<Long> getCountByEventType(@PathVariable EventType eventType) {
		log.info("REST request to get count for event: {}", eventType);
		long count = auditService.getAuditLogCountByEventType(eventType);
		return ResponseEntity.ok(count);
	}

	@GetMapping("/stats/status/{status}")
	public ResponseEntity<Long> getCountByStatus(@PathVariable ActionStatus status) {
		log.info("REST request to get count for status: {}", status);
		long count = auditService.getAuditLogCountByStatus(status);
		return ResponseEntity.ok(count);
	}
}
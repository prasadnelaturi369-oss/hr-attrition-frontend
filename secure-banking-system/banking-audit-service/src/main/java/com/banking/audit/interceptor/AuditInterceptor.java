package com.banking.audit.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.banking.audit.enums.ActionStatus;
import com.banking.audit.enums.EventType;
import com.banking.audit.enums.ResourceType;
import com.banking.audit.payload.request.AuditLogRequest;
import com.banking.audit.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuditInterceptor implements HandlerInterceptor {

	@Autowired
	private AuditService auditService;

	@Autowired
	private ObjectMapper objectMapper;

	private ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		startTime.set(System.currentTimeMillis());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		long executionTime = System.currentTimeMillis() - startTime.get();
		startTime.remove();

		AuditLogRequest auditLog = new AuditLogRequest();
		auditLog.setEventType(EventType.API_CALL);
		auditLog.setResourceType(ResourceType.SYSTEM);
		auditLog.setResourceId(request.getRequestURI());
		auditLog.setAction(request.getMethod());
		auditLog.setHttpMethod(request.getMethod());
		auditLog.setRequestUrl(request.getRequestURL().toString());
		auditLog.setIpAddress(request.getRemoteAddr());
		auditLog.setUserAgent(request.getHeader("User-Agent"));
		auditLog.setExecutionTimeMs(executionTime);
		auditLog.setStatus(response.getStatus() < 400 ? ActionStatus.SUCCESS : ActionStatus.FAILURE);
		auditLog.setServiceName("API-GATEWAY");

		auditService.saveAuditLog(auditLog);
	}
}
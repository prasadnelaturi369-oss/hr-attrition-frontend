package com.gateway.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.entity.RequestLog;
import com.gateway.repository.RequestLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

	private static final Logger log = LoggerFactory.getLogger(LogService.class);

	@Autowired
	private RequestLogRepository logRepository;

	public void saveLog(String requestPath, String method, Integer status, Long responseTime) {
		RequestLog log = new RequestLog(); 
		log.setRequestPath(requestPath);
		log.setMethod(method);
		log.setTimestamp(LocalDateTime.now());
		log.setStatus(status);
		log.setResponseTimeMs(responseTime);

		RequestLog savedLog = logRepository.save(log);

		LogService.log.info("Log saved - ID: {}, Path: {}, Method: {}, Status: {}, Time: {}ms", savedLog.getId(),
				requestPath, method, status, responseTime);
	}

	public List<RequestLog> getAllLogs() {
		return logRepository.findAll();
	}
}
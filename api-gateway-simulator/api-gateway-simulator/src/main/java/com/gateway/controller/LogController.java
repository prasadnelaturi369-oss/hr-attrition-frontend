package com.gateway.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.entity.RequestLog;
import com.gateway.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

	@Autowired
	private LogService logService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllLogs() {
		List<RequestLog> logs = logService.getAllLogs();

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("count", logs.size());
		response.put("data", logs);

		return ResponseEntity.ok(response);
	}
}
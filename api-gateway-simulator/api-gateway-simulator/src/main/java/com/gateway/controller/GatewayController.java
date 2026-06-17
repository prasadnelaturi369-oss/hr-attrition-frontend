package com.gateway.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.service.GatewayService;
import com.gateway.service.LogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private LogService logService;

	private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

	@GetMapping("/api/users")
	@Operation(summary = "Get all users", description = "Returns list of all users")
	@SecurityRequirement(name = "X-API-KEY")
	public ResponseEntity<?> getUsers(HttpServletRequest request) {
		return handleRequest("/api/users", "GET", request, null);
	}

	@GetMapping("/api/orders")
	@Operation(summary = "Get all orders", description = "Returns list of all orders")
	@SecurityRequirement(name = "X-API-KEY")
	public ResponseEntity<?> getOrders(HttpServletRequest request) {
		return handleRequest("/api/orders", "GET", request, null);
	}

	@GetMapping("/api/products")
	@Operation(summary = "Get all products", description = "Returns list of all products")
	@SecurityRequirement(name = "X-API-KEY")
	public ResponseEntity<?> getProducts(HttpServletRequest request) {
		return handleRequest("/api/products", "GET", request, null);
	}

	@PostMapping("/api/users")
	@Operation(summary = "Create a new user", description = "Adds a new user to the system")
	@SecurityRequirement(name = "X-API-KEY")
	public ResponseEntity<?> addUser(@RequestBody Map<String, Object> userData, HttpServletRequest request) {
		return handleRequest("/api/users", "POST", request, userData);
	}

	@PostMapping("/api/orders")
	@Operation(summary = "Create a new order", description = "Adds a new order to the system")
	@SecurityRequirement(name = "X-API-KEY")
	public ResponseEntity<?> addOrder(@RequestBody Map<String, Object> orderData, HttpServletRequest request) {
		return handleRequest("/api/orders", "POST", request, orderData);
	}

	@PostMapping("/api/products")
	@Operation(summary = "Create a new product", description = "Adds a new product to the system")
	@SecurityRequirement(name = "X-API-KEY")
	public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> productData, HttpServletRequest request) {
		return handleRequest("/api/products", "POST", request, productData);
	}

	private ResponseEntity<?> handleRequest(String path, String method, HttpServletRequest request,
			Object requestBody) {
		LocalDateTime requestTime = LocalDateTime.now();
		long startTime = System.currentTimeMillis();

		log.info("=== API Gateway Request ===");
		log.info("Request URL: {}", path);
		log.info("HTTP Method: {}", method);
		log.info("Request Time: {}", requestTime);
		if (requestBody != null) {
			log.info("Request Body: {}", requestBody);
		}

		try {
			Object response;
			if ("POST".equals(method)) {
				response = gatewayService.routePostRequest(path, requestBody);
			} else {
				response = gatewayService.routeRequest(path);
			}

			long responseTime = System.currentTimeMillis() - startTime;

			Integer status = (Integer) request.getAttribute("status");
			if (status == null)
				status = 200;

			log.info("Response Status: {}", status);
			log.info("Response Time: {}ms", responseTime);
			log.info("==========================");

			logService.saveLog(path, method, status, responseTime);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			long responseTime = System.currentTimeMillis() - startTime;
			log.error("Error processing request: {}", e.getMessage());
			log.info("Response Status: 500");
			log.info("Response Time: {}ms", responseTime);
			log.info("==========================");

			logService.saveLog(path, method, 500, responseTime);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Internal Server Error");
			errorResponse.put("message", e.getMessage());
			errorResponse.put("timestamp", LocalDateTime.now());

			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}
}
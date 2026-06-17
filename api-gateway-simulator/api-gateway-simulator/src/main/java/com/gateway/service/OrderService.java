package com.gateway.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final Map<Long, Map<String, Object>> orderStorage = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1005);

	public OrderService() {
		// Initialize with default orders
		orderStorage.put(1001L, createOrderMap(1001L, "ORD-001", 250.50, "PENDING", 1L, "PROD-001", 1));
		orderStorage.put(1002L, createOrderMap(1002L, "ORD-002", 499.99, "COMPLETED", 2L, "PROD-002", 1));
		orderStorage.put(1003L, createOrderMap(1003L, "ORD-003", 125.75, "SHIPPED", 1L, "PROD-003", 2));
		orderStorage.put(1004L, createOrderMap(1004L, "ORD-004", 899.00, "CANCELLED", 3L, "PROD-004", 1));
	}

	public Map<String, Object> getOrders() {
		log.debug("Fetching orders from Order Service");

		Map<String, Object> response = new HashMap<>();
		response.put("service", "Order Service");
		response.put("status", "success");
		response.put("timestamp", System.currentTimeMillis());
		response.put("count", orderStorage.size());
		response.put("data", orderStorage.values());

		return response;
	}

	public Map<String, Object> addOrder(Map<String, Object> orderRequest) {
		log.debug("Adding new order to Order Service");
		log.debug("Received order request: {}", orderRequest);

		try {
			Long newId = idGenerator.getAndIncrement();

			// Extract values from request - handle different field names
			String productId = (String) orderRequest.getOrDefault("productId", "UNKNOWN");
			Integer quantity = orderRequest.get("quantity") != null ? ((Number) orderRequest.get("quantity")).intValue()
					: 1;
			Double amount = orderRequest.get("amount") != null ? ((Number) orderRequest.get("amount")).doubleValue()
					: 0.0;

			// Generate order number
			String orderNumber = "ORD-" + System.currentTimeMillis();
			String status = "PENDING";
			Long userId = 1L; // Default user ID

			Map<String, Object> newOrder = createOrderMap(newId, orderNumber, amount, status, userId, productId,
					quantity);
			orderStorage.put(newId, newOrder);

			Map<String, Object> response = new HashMap<>();
			response.put("service", "Order Service");
			response.put("status", "success");
			response.put("message", "Order created successfully");
			response.put("data", newOrder);

			return response;

		} catch (Exception e) {
			log.error("Error creating order: {}", e.getMessage());
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("service", "Order Service");
			errorResponse.put("status", "error");
			errorResponse.put("message", "Failed to create order: " + e.getMessage());
			return errorResponse;
		}
	}

	private Map<String, Object> createOrderMap(Long id, String orderNumber, Double amount, String status, Long userId,
			String productId, Integer quantity) {
		Map<String, Object> order = new HashMap<>();
		order.put("id", id);
		order.put("orderNumber", orderNumber);
		order.put("amount", amount);
		order.put("status", status);
		order.put("userId", userId);
		order.put("productId", productId);
		order.put("quantity", quantity);
		order.put("createdAt", System.currentTimeMillis());
		return order;
	}
}
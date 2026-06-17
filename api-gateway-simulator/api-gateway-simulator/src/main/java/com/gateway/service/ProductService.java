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
public class ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	private final Map<Long, Map<String, Object>> productStorage = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(5006);

	public ProductService() {
		productStorage.put(5001L, createProductMap(5001L, "Laptop Pro", "High-performance laptop", 1299.99, 50));
		productStorage.put(5002L, createProductMap(5002L, "Wireless Mouse", "Ergonomic wireless mouse", 29.99, 200));
		productStorage.put(5003L, createProductMap(5003L, "Mechanical Keyboard", "RGB mechanical keyboard", 89.99, 75));
		productStorage.put(5004L, createProductMap(5004L, "4K Monitor", "27-inch 4K display", 399.99, 30));
		productStorage.put(5005L, createProductMap(5005L, "USB-C Hub", "7-in-1 USB-C hub", 49.99, 150));
	}

	public Map<String, Object> getProducts() {
		log.debug("Fetching products from Product Service");

		Map<String, Object> response = new HashMap<>();
		response.put("service", "Product Service");
		response.put("status", "success");
		response.put("timestamp", System.currentTimeMillis());
		response.put("count", productStorage.size());
		response.put("data", productStorage.values());

		return response;
	}

	public Map<String, Object> addProduct(Map<String, Object> productRequest) {
		log.debug("Adding new product to Product Service");
		log.debug("Received product request: {}", productRequest);

		try {
			Long newId = idGenerator.getAndIncrement();
			String name = (String) productRequest.getOrDefault("name", "Unknown Product");
			String description = (String) productRequest.getOrDefault("description", "No description");
			Double price = productRequest.get("price") != null ? ((Number) productRequest.get("price")).doubleValue()
					: 0.0;
			Integer stock = productRequest.get("stock") != null ? ((Number) productRequest.get("stock")).intValue() : 0;

			Map<String, Object> newProduct = createProductMap(newId, name, description, price, stock);
			productStorage.put(newId, newProduct);

			Map<String, Object> response = new HashMap<>();
			response.put("service", "Product Service");
			response.put("status", "success");
			response.put("message", "Product created successfully");
			response.put("data", newProduct);

			return response;

		} catch (Exception e) {
			log.error("Error creating product: {}", e.getMessage());
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("service", "Product Service");
			errorResponse.put("status", "error");
			errorResponse.put("message", "Failed to create product: " + e.getMessage());
			return errorResponse;
		}
	}

	private Map<String, Object> createProductMap(Long id, String name, String description, Double price,
			Integer stock) {
		Map<String, Object> product = new HashMap<>();
		product.put("id", id);
		product.put("name", name);
		product.put("description", description);
		product.put("price", price);
		product.put("stock", stock);
		product.put("createdAt", System.currentTimeMillis());
		return product;
	}
}
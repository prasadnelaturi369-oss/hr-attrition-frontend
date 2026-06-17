package com.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {

	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;

	private static final Logger log = LoggerFactory.getLogger(GatewayService.class);

	public Object routeRequest(String path) {
		log.debug("Routing GET request to: {}", path);

		switch (path) {
		case "/api/users":
			return userService.getUsers();
		case "/api/orders":
			return orderService.getOrders();
		case "/api/products":
			return productService.getProducts();
		default:
			throw new IllegalArgumentException("Unknown path: " + path);
		}
	}

	public Object routePostRequest(String path, Object requestBody) {
		log.debug("Routing POST request to: {}", path);

		switch (path) {
		case "/api/users":
			return userService.addUser((java.util.Map<String, Object>) requestBody);
		case "/api/orders":
			return orderService.addOrder((java.util.Map<String, Object>) requestBody);
		case "/api/products":
			return productService.addProduct((java.util.Map<String, Object>) requestBody);
		default:
			throw new IllegalArgumentException("Unknown path: " + path);
		}
	}
}
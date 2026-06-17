package com.ecommerce_cart_checkout.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce_cart_checkout.entity.Order;
import com.ecommerce_cart_checkout.payload.response.OrderResponse;
import com.ecommerce_cart_checkout.repository.OrderRepository;
import com.ecommerce_cart_checkout.service.CheckoutService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderRepository orderRepository;
	private final CheckoutService checkoutService;

	public OrderController(OrderRepository orderRepository, CheckoutService checkoutService) {
		this.orderRepository = orderRepository;
		this.checkoutService = checkoutService;
	}

	@GetMapping("/history/{userId}")
	public ResponseEntity<Page<OrderResponse>> getOrderHistory(@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Order> ordersPage = orderRepository.findOrderHistoryByUserId(userId, pageable);

		Page<OrderResponse> orderResponses = ordersPage.map(order -> checkoutService.getOrderSummary(order.getId()));

		return ResponseEntity.ok(orderResponses);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable Long orderId) {
		return ResponseEntity.ok(checkoutService.getOrderSummary(orderId));
	}
}
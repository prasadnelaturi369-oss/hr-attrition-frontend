package com.ecommerce_cart_checkout.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce_cart_checkout.entity.Order;
import com.ecommerce_cart_checkout.payload.request.CheckoutRequest;
import com.ecommerce_cart_checkout.payload.request.PaymentRequest;
import com.ecommerce_cart_checkout.payload.response.OrderResponse;
import com.ecommerce_cart_checkout.service.CheckoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

	private final CheckoutService checkoutService;

	public CheckoutController(CheckoutService checkoutService) {
		this.checkoutService = checkoutService;
	}

	@PostMapping("/create-order")
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CheckoutRequest request) {

		Order order = checkoutService.createOrderFromCart(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(checkoutService.getOrderSummary(order.getId()));
	}

	@PostMapping("/process-payment")
	public ResponseEntity<OrderResponse> processPayment(@Valid @RequestBody PaymentRequest request) {

		Order order = checkoutService.processPayment(request);

		return ResponseEntity.ok(checkoutService.getOrderSummary(order.getId()));
	}

	@GetMapping("/order-summary/{orderId}")
	public ResponseEntity<OrderResponse> getOrderSummary(@PathVariable Long orderId) {

		return ResponseEntity.ok(checkoutService.getOrderSummary(orderId));
	}
}
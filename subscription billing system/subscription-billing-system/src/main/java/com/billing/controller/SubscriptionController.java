package com.billing.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billing.payload.request.SubscriptionRequest;
import com.billing.payload.response.SubscriptionResponse;
import com.billing.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Subscription Management", description = "APIs for managing subscriptions")
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	public SubscriptionController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@PostMapping
	@Operation(summary = "Create a new subscription")
	public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody SubscriptionRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.createSubscription(request));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get user subscriptions")
	public ResponseEntity<Page<SubscriptionResponse>> getUserSubscriptions(@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId, pageable));
	}

	@GetMapping
	@Operation(summary = "Get all subscriptions")
	public ResponseEntity<Page<SubscriptionResponse>> getAllSubscriptions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(subscriptionService.getAllSubscriptions(pageable));
	}

	@GetMapping("/active")
	@Operation(summary = "Get active subscriptions")
	public ResponseEntity<Page<SubscriptionResponse>> getActiveSubscriptions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(subscriptionService.getActiveSubscriptions(pageable));
	}

	@PutMapping("/{subscriptionId}/cancel")
	@Operation(summary = "Cancel subscription")
	public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable Long subscriptionId) {
		return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId));
	}
}
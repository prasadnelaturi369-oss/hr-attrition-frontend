package com.billing.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billing.entity.Plan;
import com.billing.entity.Subscription;
import com.billing.entity.User;
import com.billing.exception.BusinessException;
import com.billing.exception.ResourceNotFoundException;
import com.billing.payload.request.SubscriptionRequest;
import com.billing.payload.response.SubscriptionResponse;
import com.billing.repository.PlanRepository;
import com.billing.repository.SubscriptionRepository;
import com.billing.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

	private final SubscriptionRepository subscriptionRepository;
	private final UserRepository userRepository;
	private final PlanRepository planRepository;
	private final AuditService auditService;

	public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository,
			PlanRepository planRepository, AuditService auditService) {
		this.subscriptionRepository = subscriptionRepository;
		this.userRepository = userRepository;
		this.planRepository = planRepository;
		this.auditService = auditService;
	}

	@Transactional
	public SubscriptionResponse createSubscription(SubscriptionRequest request) {
		log.info("Creating subscription for user: {}", request.getUserId());

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Plan plan = planRepository.findById(request.getPlanId())
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

		boolean hasActive = subscriptionRepository.existsByUserIdAndStatus(request.getUserId(), "ACTIVE");
		if (hasActive) {
			throw new BusinessException("User already has an active subscription");
		}

		LocalDateTime startDate = request.getStartDate() != null ? request.getStartDate() : LocalDateTime.now();
		LocalDateTime endDate = calculateEndDate(startDate, plan.getBillingCycle());

		Subscription subscription = new Subscription();
		subscription.setUser(user);
		subscription.setPlan(plan);
		subscription.setStartDate(startDate);
		subscription.setEndDate(endDate);
		subscription.setStatus("ACTIVE");
		subscription.setAmount(plan.getPrice());
		subscription.setBillingCycle(plan.getBillingCycle());

		Subscription savedSubscription = subscriptionRepository.save(subscription);

		auditService.log("Subscription", savedSubscription.getId(), "CREATED", null,
				savedSubscription.getSubscriptionNumber(), "Subscription created");

		// Generate invoice for this subscription
		generateInvoice(savedSubscription);

		return mapToResponse(savedSubscription);
	}

	private LocalDateTime calculateEndDate(LocalDateTime startDate, String billingCycle) {
		return switch (billingCycle.toUpperCase()) {
		case "MONTHLY" -> startDate.plusMonths(1);
		case "QUARTERLY" -> startDate.plusMonths(3);
		case "YEARLY" -> startDate.plusYears(1);
		default -> startDate.plusMonths(1);
		};
	}

	private void generateInvoice(Subscription subscription) {
		// Invoice generation logic - will be implemented in InvoiceService
		log.info("Generating invoice for subscription: {}", subscription.getSubscriptionNumber());
	}

	@Transactional
	public SubscriptionResponse cancelSubscription(Long subscriptionId) {
		Subscription subscription = subscriptionRepository.findById(subscriptionId)
				.orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

		String oldStatus = subscription.getStatus();
		subscription.setStatus("CANCELLED");
		subscription.setCancelledAt(LocalDateTime.now());

		Subscription cancelledSubscription = subscriptionRepository.save(subscription);

		auditService.log("Subscription", cancelledSubscription.getId(), "CANCELLED", oldStatus, "CANCELLED",
				"Subscription cancelled");

		return mapToResponse(cancelledSubscription);
	}

	public Page<SubscriptionResponse> getUserSubscriptions(Long userId, Pageable pageable) {
		return subscriptionRepository.findByUserId(userId, pageable).map(this::mapToResponse);
	}

	public Page<SubscriptionResponse> getAllSubscriptions(Pageable pageable) {
		return subscriptionRepository.findAll(pageable).map(this::mapToResponse);
	}

	public Page<SubscriptionResponse> getActiveSubscriptions(Pageable pageable) {
		return subscriptionRepository.findByStatus("ACTIVE", pageable).map(this::mapToResponse);
	}

	private SubscriptionResponse mapToResponse(Subscription subscription) {
		SubscriptionResponse response = new SubscriptionResponse();
		response.setId(subscription.getId());
		response.setSubscriptionNumber(subscription.getSubscriptionNumber());
		response.setUserId(subscription.getUser().getId());
		response.setUserName(subscription.getUser().getFullName());
		response.setPlanId(subscription.getPlan().getId());
		response.setPlanName(subscription.getPlan().getName());
		response.setStartDate(subscription.getStartDate());
		response.setEndDate(subscription.getEndDate());
		response.setStatus(subscription.getStatus());
		response.setAmount(subscription.getAmount());
		response.setBillingCycle(subscription.getBillingCycle());
		response.setCreatedAt(subscription.getCreatedAt());
		return response;
	}
}
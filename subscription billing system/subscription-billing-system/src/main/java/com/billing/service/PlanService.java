package com.billing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billing.entity.Plan;
import com.billing.exception.BusinessException;
import com.billing.exception.ResourceNotFoundException;
import com.billing.payload.request.PlanRequest;
import com.billing.payload.response.PlanResponse;
import com.billing.repository.PlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {

	private static final Logger log = LoggerFactory.getLogger(PlanService.class);

	private final PlanRepository planRepository;
	private final AuditService auditService;

	public PlanService(PlanRepository planRepository, AuditService auditService) {
		this.planRepository = planRepository;
		this.auditService = auditService;
	}

	@Transactional
	public PlanResponse createPlan(PlanRequest request) {
		log.info("Creating new plan: {}", request.getName());

		if (planRepository.existsByName(request.getName())) {
			throw new BusinessException("Plan with name " + request.getName() + " already exists");
		}

		Plan plan = new Plan();
		plan.setName(request.getName());
		plan.setDescription(request.getDescription());
		plan.setPrice(request.getPrice());
		plan.setBillingCycle(request.getBillingCycle());
		plan.setFeatures(request.getFeatures());

		Plan savedPlan = planRepository.save(plan);

		auditService.log("Plan", savedPlan.getId(), "CREATED", null, savedPlan.getName(), "Plan created");

		return mapToResponse(savedPlan);
	}

	public PlanResponse getPlanById(Long id) {
		Plan plan = planRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
		return mapToResponse(plan);
	}

	public Page<PlanResponse> getAllPlans(Pageable pageable) {
		return planRepository.findAll(pageable).map(this::mapToResponse);
	}

	@Transactional
	public PlanResponse updatePlan(Long id, PlanRequest request) {
		Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

		String oldName = plan.getName();

		if (request.getName() != null)
			plan.setName(request.getName());
		if (request.getDescription() != null)
			plan.setDescription(request.getDescription());
		if (request.getPrice() != null)
			plan.setPrice(request.getPrice());
		if (request.getBillingCycle() != null)
			plan.setBillingCycle(request.getBillingCycle());
		if (request.getFeatures() != null)
			plan.setFeatures(request.getFeatures());

		Plan updatedPlan = planRepository.save(plan);

		auditService.log("Plan", updatedPlan.getId(), "UPDATED", oldName, updatedPlan.getName(), "Plan updated");

		return mapToResponse(updatedPlan);
	}

	@Transactional
	public void deletePlan(Long id) {
		Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

		plan.setStatus("INACTIVE");
		planRepository.save(plan);

		auditService.log("Plan", plan.getId(), "DELETED", plan.getName(), null, "Plan deactivated");
	}

	private PlanResponse mapToResponse(Plan plan) {
		PlanResponse response = new PlanResponse();
		response.setId(plan.getId());
		response.setName(plan.getName());
		response.setDescription(plan.getDescription());
		response.setPrice(plan.getPrice());
		response.setBillingCycle(plan.getBillingCycle());
		response.setFeatures(plan.getFeatures());
		response.setStatus(plan.getStatus());
		response.setCreatedAt(plan.getCreatedAt());
		return response;
	}
}
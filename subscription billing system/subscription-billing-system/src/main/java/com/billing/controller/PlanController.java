package com.billing.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billing.payload.request.PlanRequest;
import com.billing.payload.response.PlanResponse;
import com.billing.service.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Plan Management", description = "APIs for managing subscription plans")
public class PlanController {

	private final PlanService planService;

	public PlanController(PlanService planService) {
		this.planService = planService;
	}

	@PostMapping
	@Operation(summary = "Create a new plan")
	public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody PlanRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(request));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get plan by ID")
	public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long id) {
		return ResponseEntity.ok(planService.getPlanById(id));
	}

	@GetMapping
	@Operation(summary = "Get all plans with pagination")
	public ResponseEntity<Page<PlanResponse>> getAllPlans(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
		return ResponseEntity.ok(planService.getAllPlans(pageable));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update plan")
	public ResponseEntity<PlanResponse> updatePlan(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
		return ResponseEntity.ok(planService.updatePlan(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete plan")
	public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
		planService.deletePlan(id);
		return ResponseEntity.noContent().build();
	}
}
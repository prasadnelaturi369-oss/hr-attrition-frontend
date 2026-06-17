package com.hrms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.hrms.payload.request.LeaveRequestPayload;
import com.hrms.payload.response.LeaveResponse;
import com.hrms.service.LeaveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Leave Management", description = "APIs for managing leave requests")
public class LeaveController {

	private final LeaveService leaveService;

	public LeaveController(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	@PostMapping("/apply")
	@Operation(summary = "Apply for leave")
	public ResponseEntity<LeaveResponse> applyLeave(@Valid @RequestBody LeaveRequestPayload request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.applyLeave(request));
	}

	@PutMapping("/{leaveId}/approve")
	@Operation(summary = "Approve leave request")
	public ResponseEntity<LeaveResponse> approveLeave(@PathVariable Long leaveId, @RequestParam Long approverId) {
		return ResponseEntity.ok(leaveService.approveLeave(leaveId, approverId));
	}

	@PutMapping("/{leaveId}/reject")
	@Operation(summary = "Reject leave request")
	public ResponseEntity<LeaveResponse> rejectLeave(@PathVariable Long leaveId, @RequestParam String reason) {
		return ResponseEntity.ok(leaveService.rejectLeave(leaveId, reason));
	}

	@GetMapping("/employee/{employeeId}")
	@Operation(summary = "Get leaves by employee")
	public ResponseEntity<Page<LeaveResponse>> getLeavesByEmployee(@PathVariable Long employeeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("requestedAt").descending());
		return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId, pageable));
	}

	@GetMapping("/pending")
	@Operation(summary = "Get all pending leaves")
	public ResponseEntity<Page<LeaveResponse>> getPendingLeaves(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("requestedAt").descending());
		return ResponseEntity.ok(leaveService.getPendingLeaves(pageable));
	}
}
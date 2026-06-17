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

import com.hrms.payload.request.PayrollRequest;
import com.hrms.payload.response.PayrollResponse;
import com.hrms.service.PayrollService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payroll Management", description = "APIs for managing payroll")
public class PayrollController {

	private final PayrollService payrollService;

	public PayrollController(PayrollService payrollService) {
		this.payrollService = payrollService;
	}

	@PostMapping("/generate")
	@Operation(summary = "Generate payroll for employee")
	public ResponseEntity<PayrollResponse> generatePayroll(@Valid @RequestBody PayrollRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(payrollService.generatePayroll(request));
	}

	@GetMapping("/employee/{employeeId}")
	@Operation(summary = "Get payroll by employee")
	public ResponseEntity<Page<PayrollResponse>> getPayrollByEmployee(@PathVariable Long employeeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("generatedAt").descending());
		return ResponseEntity.ok(payrollService.getPayrollByEmployee(employeeId, pageable));
	}

	@GetMapping("/month/{month}")
	@Operation(summary = "Get payroll by month")
	public ResponseEntity<Page<PayrollResponse>> getPayrollByMonth(@PathVariable String month,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(payrollService.getPayrollByMonth(month, pageable));
	}

	@PutMapping("/{payrollId}/process")
	@Operation(summary = "Process payroll")
	public ResponseEntity<PayrollResponse> processPayroll(@PathVariable Long payrollId) {
		return ResponseEntity.ok(payrollService.processPayroll(payrollId));
	}
}
package com.banking.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.payload.request.CustomerRequest;
import com.banking.payload.response.CustomerResponse;
import com.banking.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing bank customers")
public class CustomerController {

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	@Operation(summary = "Create a new customer")
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
		log.info("POST /api/customers - Create customer");
		return new ResponseEntity<>(customerService.createCustomer(request), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get customer by ID")
	public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
		log.info("GET /api/customers/{} - Get customer", id);
		return ResponseEntity.ok(customerService.getCustomer(id));
	}
}
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

import com.banking.payload.request.AccountRequest;
import com.banking.payload.response.AccountResponse;
import com.banking.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(AccountController.class);
	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/customer/{customerId}")
	@Operation(summary = "Create account for customer")
	public ResponseEntity<AccountResponse> createAccount(@PathVariable Long customerId,
			@Valid @RequestBody AccountRequest request) {
		log.info("POST /api/accounts/customer/{} - Create account", customerId);
		return new ResponseEntity<>(accountService.createAccount(customerId, request), HttpStatus.CREATED);
	}

	@GetMapping("/{accountNumber}")
	@Operation(summary = "Get account by account number")
	public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
		log.info("GET /api/accounts/{} - Get account", accountNumber);
		return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
	}
}
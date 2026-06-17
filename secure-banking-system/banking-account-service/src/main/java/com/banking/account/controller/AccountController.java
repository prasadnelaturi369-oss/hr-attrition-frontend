package com.banking.account.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.account.entity.AccountStatus;
import com.banking.account.payload.request.AccountRequest;
import com.banking.account.payload.response.AccountResponse;
import com.banking.account.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;

	@PostMapping
	public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request,
			@RequestHeader(value = "X-User-Id", required = true) UUID userId) {
		log.info("REST request to create account for user: {}", userId);
		AccountResponse response = accountService.createAccount(request, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<AccountResponse>> getUserAccounts(@PathVariable UUID userId) {
		log.info("REST request to get all accounts for user: {}", userId);
		List<AccountResponse> accounts = accountService.getUserAccounts(userId);
		return ResponseEntity.ok(accounts);
	}

	@GetMapping("/user/{userId}/active")
	public ResponseEntity<List<AccountResponse>> getActiveAccounts(@PathVariable UUID userId) {
		log.info("REST request to get active accounts for user: {}", userId);
		List<AccountResponse> accounts = accountService.getActiveAccounts(userId);
		return ResponseEntity.ok(accounts);
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID accountId) {
		log.info("REST request to get account: {}", accountId);
		AccountResponse account = accountService.getAccount(accountId);
		return ResponseEntity.ok(account);
	}

	@GetMapping("/{accountId}/balance")
	public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID accountId) {
		log.info("REST request to get balance for account: {}", accountId);
		BigDecimal balance = accountService.getBalance(accountId);
		return ResponseEntity.ok(balance);
	}

	@PostMapping("/{accountId}/debit")
	public ResponseEntity<Void> debit(@PathVariable UUID accountId, @RequestParam BigDecimal amount,
			@RequestParam String reference) {
		log.info("REST request to debit {} from account: {}", amount, accountId);
		accountService.debit(accountId, amount, reference);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{accountId}/credit")
	public ResponseEntity<Void> credit(@PathVariable UUID accountId, @RequestParam BigDecimal amount,
			@RequestParam String reference) {
		log.info("REST request to credit {} to account: {}", amount, accountId);
		accountService.credit(accountId, amount, reference);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{accountId}/status")
	public ResponseEntity<AccountResponse> updateStatus(@PathVariable UUID accountId,
			@RequestParam AccountStatus status) {
		log.info("REST request to update account {} status to: {}", accountId, status);
		AccountResponse response = accountService.updateAccountStatus(accountId, status);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/{userId}/count")
	public ResponseEntity<Long> getAccountCount(@PathVariable UUID userId) {
		log.info("REST request to get account count for user: {}", userId);
		long count = accountService.getAccountCount(userId);
		return ResponseEntity.ok(count);
	}
}
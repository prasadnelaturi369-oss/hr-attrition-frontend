package com.banking.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.account.entity.Account;
import com.banking.account.payload.request.AccountRequest;
import com.banking.account.payload.response.AccountResponse;
import com.banking.account.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping
	public AccountResponse createAccount(@RequestBody AccountRequest request,
			@RequestHeader("X-User-Id") String userId) {
		return accountService.createAccount(request, userId);
	}

	@GetMapping("/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		return accountService.getAccount(accountId);
	}

	@GetMapping("/customer")
	public List<Account> getCustomerAccounts(@RequestHeader("X-User-Id") String userId) {
		return accountService.getCustomerAccounts(userId);
	}

	@GetMapping("/{accountId}/balance")
	public java.math.BigDecimal getBalance(@PathVariable String accountId) {
		return accountService.getBalance(accountId);
	}
}
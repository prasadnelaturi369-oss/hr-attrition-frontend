package com.banking.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.account.entity.Account;
import com.banking.account.payload.request.AccountRequest;
import com.banking.account.payload.response.AccountResponse;
import com.banking.account.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public AccountResponse createAccount(AccountRequest request, String customerId) {
		Account account = new Account();
		account.setCustomerId(customerId);
		account.setAccountType(request.getAccountType());
		account.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
		account.setBalance(BigDecimal.ZERO);
		account.setActive(true);

		Account saved = accountRepository.save(account);
		return new AccountResponse(saved.getId(), saved.getAccountNumber(), saved.getAccountType(), saved.getBalance());
	}

	public Account getAccount(String accountId) {
		return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
	}

	public List<Account> getCustomerAccounts(String customerId) {
		return accountRepository.findByCustomerId(customerId);
	}

	public BigDecimal getBalance(String accountId) {
		Account account = getAccount(accountId);
		return account.getBalance();
	}
}
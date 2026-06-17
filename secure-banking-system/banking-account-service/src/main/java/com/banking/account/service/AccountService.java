package com.banking.account.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.account.entity.Account;
import com.banking.account.entity.AccountStatus;
import com.banking.account.payload.request.AccountRequest;
import com.banking.account.payload.response.AccountResponse;
import com.banking.account.repository.AccountRepository;

@Service
public class AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AccountRepository accountRepository;

	@Transactional
	public AccountResponse createAccount(AccountRequest request, UUID userId) {
		log.info("Creating new {} account for user: {}", request.getAccountType(), userId);

		// Check if user already has an account of same type
		if (accountRepository.existsByUserIdAndAccountType(userId, request.getAccountType())) {
			throw new RuntimeException("User already has a " + request.getAccountType() + " account");
		}

		Account account = new Account();
		account.setUserId(userId);
		account.setAccountType(request.getAccountType());
		account.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
		account.setBalance(BigDecimal.ZERO);
		account.setStatus(AccountStatus.ACTIVE);

		Account savedAccount = accountRepository.save(account);
		log.info("Account created successfully. Account Number: {}", savedAccount.getAccountNumber());

		return AccountResponse.fromEntity(savedAccount);
	}

	public List<AccountResponse> getUserAccounts(UUID userId) {
		log.debug("Fetching all accounts for user: {}", userId);
		return accountRepository.findByUserId(userId).stream().map(AccountResponse::fromEntity)
				.collect(Collectors.toList());
	}

	public AccountResponse getAccount(UUID accountId) {
		log.debug("Fetching account details for account: {}", accountId);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
		return AccountResponse.fromEntity(account);
	}

	public BigDecimal getBalance(UUID accountId) {
		log.debug("Fetching balance for account: {}", accountId);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
		return account.getBalance();
	}

	public List<AccountResponse> getActiveAccounts(UUID userId) {
		log.debug("Fetching active accounts for user: {}", userId);
		return accountRepository.findByUserIdAndStatus(userId, AccountStatus.ACTIVE).stream()
				.map(AccountResponse::fromEntity).collect(Collectors.toList());
	}

	@Transactional
	@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 100, multiplier = 2))
	public void debit(UUID accountId, BigDecimal amount, String reference) {
		log.info("Debiting {} from account: {}, reference: {}", amount, accountId, reference);

		Account account = accountRepository.findByIdWithLock(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new RuntimeException("Account is not active. Status: " + account.getStatus());
		}

		account.debit(amount);
		accountRepository.save(account);

		log.info("Debit successful. New balance for account {}: {}", accountId, account.getBalance());
	}

	@Transactional
	@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 100, multiplier = 2))
	public void credit(UUID accountId, BigDecimal amount, String reference) {
		log.info("Crediting {} to account: {}, reference: {}", amount, accountId, reference);

		Account account = accountRepository.findByIdWithLock(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new RuntimeException("Account is not active. Status: " + account.getStatus());
		}

		account.credit(amount);
		accountRepository.save(account);

		log.info("Credit successful. New balance for account {}: {}", accountId, account.getBalance());
	}

	@Transactional
	public AccountResponse updateAccountStatus(UUID accountId, AccountStatus status) {
		log.info("Updating account {} status to: {}", accountId, status);

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

		account.setStatus(status);
		Account updated = accountRepository.save(account);

		return AccountResponse.fromEntity(updated);
	}

	public long getAccountCount(UUID userId) {
		return accountRepository.countByUserId(userId);
	}
}
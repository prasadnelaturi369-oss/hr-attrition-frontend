package com.banking.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.entity.Account;
import com.banking.entity.Customer;
import com.banking.enums.AccountType;
import com.banking.exception.BankingException;
import com.banking.payload.request.AccountRequest;
import com.banking.payload.response.AccountResponse;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;

@Service
public class AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountService.class);
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AuditService auditService;

	public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository,
			AuditService auditService) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.auditService = auditService;
	}

	@Transactional
	public AccountResponse createAccount(Long customerId, AccountRequest request) {
		log.info("Creating account for customer: {}", customerId);

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new BankingException("Customer not found"));

		Account account = new Account();
		account.setAccountType(AccountType.valueOf(request.getAccountType()));
		account.setCurrency(request.getCurrency());
		account.setBalance(request.getInitialBalance());
		account.setCustomer(customer);

		if (account.getAccountType() == AccountType.CURRENT) {
			account.setOverdraftLimit(new BigDecimal("1000"));
		}

		Account saved = accountRepository.save(account);

		auditService.log("CREATE_ACCOUNT", "Account", String.valueOf(saved.getId()),
				"Account created: " + saved.getAccountNumber());
		AccountResponse response = new AccountResponse();
		response.setAccountNumber(saved.getAccountNumber());
		response.setAccountType(saved.getAccountType().name());
		response.setBalance(saved.getBalance());
		response.setCurrency(saved.getCurrency());
		response.setIsActive(saved.getIsActive());
		response.setCreatedAt(saved.getCreatedAt());
		response.setCustomerName(customer.getFirstName() + " " + customer.getLastName());

		return AccountResponse.success(response, "Account created successfully");
	}

	public AccountResponse getAccountByNumber(String accountNumber) {
		log.info("Fetching account: {}", accountNumber);

		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BankingException("Account not found"));

		AccountResponse response = new AccountResponse();
		response.setAccountNumber(account.getAccountNumber());
		response.setAccountType(account.getAccountType().name());
		response.setBalance(account.getBalance());
		response.setCurrency(account.getCurrency());
		response.setIsActive(account.getIsActive());

		return AccountResponse.success(response, "Account found");
	}

	public void validateSufficientBalance(String accountNumber, BigDecimal amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BankingException("Account not found"));

		BigDecimal available = account.getBalance().add(account.getOverdraftLimit());

		if (available.compareTo(amount) < 0) {
			throw new BankingException("Insufficient balance. Available: ₹" + available);
		}
	}
}
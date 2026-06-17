package com.banking.transaction.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.banking.transaction.exception.TransactionException;

@Component
public class AccountServiceClient {

	private static final Logger log = LoggerFactory.getLogger(AccountServiceClient.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${account.service.url:http://account-service}")
	private String accountServiceUrl;

	/**
	 * Debit amount from account
	 */
	@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 100, multiplier = 2))
	public void debitAccount(UUID accountId, BigDecimal amount, String reference) {
		log.info("Calling account service to debit {} from account: {}, reference: {}", amount, accountId, reference);

		String url = accountServiceUrl + "/api/accounts/" + accountId + "/debit?amount=" + amount + "&reference="
				+ reference;

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("amount", amount);
			requestBody.put("reference", reference);

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new TransactionException(
						"Failed to debit account: " + accountId + ", Status: " + response.getStatusCode());
			}
			log.info("Successfully debited {} from account: {}", amount, accountId);
		} catch (RestClientException e) {
			log.error("Error debiting account {}: {}", accountId, e.getMessage());
			throw new TransactionException("Error debiting account: " + e.getMessage());
		}
	}

	/**
	 * Credit amount to account
	 */
	@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 100, multiplier = 2))
	public void creditAccount(UUID accountId, BigDecimal amount, String reference) {
		log.info("Calling account service to credit {} to account: {}, reference: {}", amount, accountId, reference);

		String url = accountServiceUrl + "/api/accounts/" + accountId + "/credit?amount=" + amount + "&reference="
				+ reference;

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("amount", amount);
			requestBody.put("reference", reference);

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new TransactionException(
						"Failed to credit account: " + accountId + ", Status: " + response.getStatusCode());
			}
			log.info("Successfully credited {} to account: {}", amount, accountId);
		} catch (RestClientException e) {
			log.error("Error crediting account {}: {}", accountId, e.getMessage());
			throw new TransactionException("Error crediting account: " + e.getMessage());
		}
	}

	/**
	 * Validate if account exists
	 */
	public boolean validateAccount(UUID accountId) {
		log.info("Validating account: {}", accountId);

		String url = accountServiceUrl + "/api/accounts/" + accountId;

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			boolean isValid = response.getStatusCode().is2xxSuccessful();
			log.info("Account {} validation result: {}", accountId, isValid);
			return isValid;
		} catch (RestClientException e) {
			log.warn("Account validation failed for {}: {}", accountId, e.getMessage());
			return false;
		}
	}

	/**
	 * Get account balance
	 */
	public BigDecimal getAccountBalance(UUID accountId) {
		log.info("Fetching balance for account: {}", accountId);

		String url = accountServiceUrl + "/api/accounts/" + accountId + "/balance";

		try {
			ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
			BigDecimal balance = response.getBody();
			log.info("Account {} balance: {}", accountId, balance);
			return balance != null ? balance : BigDecimal.ZERO;
		} catch (RestClientException e) {
			log.error("Error fetching account balance: {}", e.getMessage());
			throw new TransactionException("Error fetching account balance: " + e.getMessage());
		}
	}

	/**
	 * Get account details
	 */
	public AccountDetails getAccountDetails(UUID accountId) {
		log.info("Fetching account details for: {}", accountId);

		String url = accountServiceUrl + "/api/accounts/" + accountId;

		try {
			ResponseEntity<AccountDetails> response = restTemplate.getForEntity(url, AccountDetails.class);
			return response.getBody();
		} catch (RestClientException e) {
			log.error("Error fetching account details: {}", e.getMessage());
			throw new TransactionException("Error fetching account details: " + e.getMessage());
		}
	}

	/**
	 * Account Details DTO
	 */
	public static class AccountDetails {
		private UUID id;
		private String accountNumber;
		private UUID userId;
		private String accountType;
		private BigDecimal balance;
		private String currency;
		private String status;

		// Getters and Setters
		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public UUID getUserId() {
			return userId;
		}

		public void setUserId(UUID userId) {
			this.userId = userId;
		}

		public String getAccountType() {
			return accountType;
		}

		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}

		public BigDecimal getBalance() {
			return balance;
		}

		public void setBalance(BigDecimal balance) {
			this.balance = balance;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
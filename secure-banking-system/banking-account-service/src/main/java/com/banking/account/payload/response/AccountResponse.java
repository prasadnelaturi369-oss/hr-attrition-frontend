package com.banking.account.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.banking.account.entity.Account;

public class AccountResponse {

	private UUID id;
	private String accountNumber;
	private String accountType;
	private BigDecimal balance;
	private String currency;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public AccountResponse() {
	}

	public AccountResponse(UUID id, String accountNumber, String accountType, BigDecimal balance, String currency,
			String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.currency = currency;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static AccountResponse fromEntity(Account account) {
		return new AccountResponse(account.getId(), account.getAccountNumber(), account.getAccountType().name(),
				account.getBalance(), account.getCurrency(), account.getStatus().name(), account.getCreatedAt(),
				account.getUpdatedAt());
	}

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
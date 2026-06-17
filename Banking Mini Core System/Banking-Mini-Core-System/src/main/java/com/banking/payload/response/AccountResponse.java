package com.banking.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {
	private String accountNumber;
	private String accountType;
	private BigDecimal balance;
	private String currency;
	private Boolean isActive;
	private LocalDateTime createdAt;
	private String customerName;
	private String message;
	private boolean success;

	public static AccountResponse success(AccountResponse data, String message) {
		data.setSuccess(true);
		data.setMessage(message);
		return data;
	}

	public static AccountResponse error(String message) {
		AccountResponse response = new AccountResponse();
		response.setSuccess(false);
		response.setMessage(message);
		return response;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
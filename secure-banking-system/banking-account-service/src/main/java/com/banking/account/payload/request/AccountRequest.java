package com.banking.account.payload.request;

import com.banking.account.entity.AccountType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AccountRequest {

	@NotNull(message = "Account type is required")
	private AccountType accountType;

	@Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
	private String currency = "USD";

	// Getters and Setters
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
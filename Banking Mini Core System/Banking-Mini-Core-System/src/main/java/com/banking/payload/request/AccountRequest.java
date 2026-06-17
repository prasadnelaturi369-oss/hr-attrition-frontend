package com.banking.payload.request;

import java.math.BigDecimal;

public class AccountRequest {
	private String accountType;

	private BigDecimal initialBalance = BigDecimal.ZERO;

	private String currency = "INR";

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
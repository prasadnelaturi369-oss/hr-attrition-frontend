package com.banking.account.payload.response;

import java.math.BigDecimal;

public class AccountResponse {
	private String id;
	private String accountNumber;
	private String accountType;
	private BigDecimal balance;

	public AccountResponse(String id, String accountNumber, String accountType, BigDecimal balance) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}
}
package com.banking.payload.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WithdrawRequest {
	private String accountNumber;

	private BigDecimal amount;

	private String description;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
package com.banking.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
	private Long transactionId;
	private String transactionReference;
	private String transactionType;
	private String status;
	private BigDecimal amount;
	private BigDecimal balanceAfter;
	private String description;
	private LocalDateTime transactionDate;
	private String message;
	private boolean success;

	public static TransactionResponse success(TransactionResponse data, String message) {
		data.setSuccess(true);
		data.setMessage(message);
		return data;
	}

	public static TransactionResponse error(String message) {
		TransactionResponse response = new TransactionResponse();
		response.setSuccess(false);
		response.setMessage(message);
		return response;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
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
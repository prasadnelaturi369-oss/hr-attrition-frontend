package com.banking.transaction.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.banking.transaction.entity.Transaction;

public class TransactionHistoryResponse {
	private UUID id;
	private String transactionRef;
	private UUID fromAccountId;
	private UUID toAccountId;
	private BigDecimal amount;
	private String currency;
	private String description;
	private String status;
	private String failureReason;
	private LocalDateTime createdAt;
	private LocalDateTime completedAt;
	private String transactionType;

	public static TransactionHistoryResponse fromEntity(Transaction transaction) {
		TransactionHistoryResponse response = new TransactionHistoryResponse();
		response.setId(transaction.getId());
		response.setTransactionRef(transaction.getTransactionRef());
		response.setFromAccountId(transaction.getFromAccountId());
		response.setToAccountId(transaction.getToAccountId());
		response.setAmount(transaction.getAmount());
		response.setCurrency(transaction.getCurrency());
		response.setDescription(transaction.getDescription());
		response.setStatus(transaction.getStatus().name());
		response.setFailureReason(transaction.getFailureReason());
		response.setCreatedAt(transaction.getCreatedAt());
		response.setCompletedAt(transaction.getCompletedAt());
		return response;
	}

	public static TransactionHistoryResponse fromEntityWithType(Transaction transaction, UUID accountId) {
		TransactionHistoryResponse response = fromEntity(transaction);
		if (transaction.getFromAccountId().equals(accountId)) {
			response.setTransactionType("DEBIT");
		} else if (transaction.getToAccountId().equals(accountId)) {
			response.setTransactionType("CREDIT");
		}
		return response;
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public UUID getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(UUID fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public UUID getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(UUID toAccountId) {
		this.toAccountId = toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
package com.banking.transaction.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.banking.transaction.entity.TransactionStatus;

public class TransactionEvent {
	private String eventId;
	private String transactionRef;
	private UUID fromAccountId;
	private UUID toAccountId;
	private BigDecimal amount;
	private String currency;
	private TransactionStatus status;
	private String message;
	private LocalDateTime timestamp;

	public TransactionEvent(String transactionRef, UUID fromAccountId, UUID toAccountId, BigDecimal amount,
			String currency, TransactionStatus status, String message) {
		this.eventId = UUID.randomUUID().toString();
		this.transactionRef = transactionRef;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
		this.currency = currency;
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	// Getters and Setters
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
package com.banking.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
	private String id;
	private String fromAccountId;
	private String toAccountId;
	private BigDecimal amount;
	private String currency;
	private String description;
	private String status;
	private String reference;
	private LocalDateTime createdAt;

	public Transaction() {
		this.id = UUID.randomUUID().toString();
		this.reference = "TXN" + System.currentTimeMillis();
		this.status = "COMPLETED";
		this.createdAt = LocalDateTime.now();
	}

	public String getId() {
		return id;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public String getReference() {
		return reference;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	// Setters
	public void setId(String id) {
		this.id = id;
	}

	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
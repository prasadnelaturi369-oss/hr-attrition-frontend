package com.banking.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "transactions", indexes = { @Index(name = "idx_transaction_ref", columnList = "transactionRef"),
		@Index(name = "idx_from_account", columnList = "fromAccountId"),
		@Index(name = "idx_to_account", columnList = "toAccountId"), @Index(name = "idx_status", columnList = "status"),
		@Index(name = "idx_created_at", columnList = "createdAt") })
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(unique = true, nullable = false)
	private String transactionRef;

	@Column(nullable = false)
	private UUID fromAccountId;

	@Column(nullable = false)
	private UUID toAccountId;

	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal amount;

	@Column(length = 3)
	private String currency = "USD";

	@Column(length = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionStatus status;

	@Column(length = 1000)
	private String failureReason;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime completedAt;

	@Version
	private Long version;

	public Transaction() {
		this.createdAt = LocalDateTime.now();
		this.status = TransactionStatus.PENDING;
	}

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
		if (status == null) {
			status = TransactionStatus.PENDING;
		}
		generateTransactionReference();
	}

	private void generateTransactionReference() {
		this.transactionRef = "TXN" + System.currentTimeMillis()
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
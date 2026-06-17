package com.banking.transaction.payload.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransferResponse {
	private UUID transactionId;
	private String transactionRef;
	private String status;
	private String message;
	private LocalDateTime timestamp;

	public TransferResponse(UUID transactionId, String transactionRef, String status, String message) {
		this.transactionId = transactionId;
		this.transactionRef = transactionRef;
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	// Getters and Setters
	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
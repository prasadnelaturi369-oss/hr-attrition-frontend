package com.banking.transaction.entity;

public enum TransactionStatus {
	PENDING("Pending"), PROCESSING("Processing"), COMPLETED("Completed"), FAILED("Failed"), ROLLED_BACK("Rolled Back");

	private final String description;

	TransactionStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
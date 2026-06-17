package com.banking.account.entity;

public enum AccountStatus {
	ACTIVE("Active"), FROZEN("Frozen"), CLOSED("Closed");

	private final String description;

	AccountStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
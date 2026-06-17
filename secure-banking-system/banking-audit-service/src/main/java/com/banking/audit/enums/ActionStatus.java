package com.banking.audit.enums;

public enum ActionStatus {
	SUCCESS("Operation completed successfully"), FAILURE("Operation failed"), PENDING("Operation in progress"),
	SKIPPED("Operation skipped");

	private final String description;

	ActionStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
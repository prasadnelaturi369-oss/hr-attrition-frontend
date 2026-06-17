package com.banking.audit.enums;

public enum EventType {
	USER_REGISTER("User Registration"), USER_LOGIN("User Login"), USER_LOGOUT("User Logout"),

	ACCOUNT_CREATE("Account Creation"), ACCOUNT_UPDATE("Account Update"), ACCOUNT_DELETE("Account Deletion"),
	ACCOUNT_STATUS_CHANGE("Account Status Change"), ACCOUNT_BALANCE_CHECK("Balance Check"),

	TRANSACTION_INITIATE("Transaction Initiation"), TRANSACTION_COMPLETE("Transaction Completion"),
	TRANSACTION_FAILED("Transaction Failed"), TRANSACTION_ROLLBACK("Transaction Rollback"),

	DEBIT_OPERATION("Debit Operation"), CREDIT_OPERATION("Credit Operation"),

	API_CALL("API Call"), AUTH_FAILURE("Authentication Failure"), AUTHORIZATION_FAILURE("Authorization Failure"),

	SYSTEM_ERROR("System Error"), SYSTEM_STARTUP("System Startup"), SYSTEM_SHUTDOWN("System Shutdown");

	private final String description;

	EventType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
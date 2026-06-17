package com.banking.transaction.payload.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class TransferRequest {

	@NotNull(message = "From account ID is required")
	private UUID fromAccountId;

	@NotNull(message = "To account ID is required")
	private UUID toAccountId;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	@DecimalMin(value = "0.01", message = "Minimum transfer amount is 0.01")
	@DecimalMax(value = "100000", message = "Maximum transfer amount is 100,000")
	private BigDecimal amount;

	@Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
	private String currency = "USD";

	@Size(max = 200, message = "Description cannot exceed 200 characters")
	private String description;

	// Getters and Setters
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
}
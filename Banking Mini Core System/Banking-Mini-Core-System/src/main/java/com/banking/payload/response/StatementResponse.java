package com.banking.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class StatementResponse {
	private String accountNumber;
	private String customerName;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private BigDecimal openingBalance;
	private BigDecimal closingBalance;
	private List<StatementEntry> transactions;
	private String message;
	private boolean success;

	public static class StatementEntry {
		private LocalDateTime date;
		private String type;
		private BigDecimal amount;
		private String description;
		private BigDecimal balanceAfter;

		public LocalDateTime getDate() {
			return date;
		}

		public void setDate(LocalDateTime date) {
			this.date = date;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public BigDecimal getBalanceAfter() {
			return balanceAfter;
		}

		public void setBalanceAfter(BigDecimal balanceAfter) {
			this.balanceAfter = balanceAfter;
		}

	}

	public static StatementResponse success(StatementResponse data, String message) {
		data.setSuccess(true);
		data.setMessage(message);
		return data;
	}

	public static StatementResponse error(String message) {
		StatementResponse response = new StatementResponse();
		response.setSuccess(false);
		response.setMessage(message);
		return response;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LocalDateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDateTime fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDateTime getToDate() {
		return toDate;
	}

	public void setToDate(LocalDateTime toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public BigDecimal getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}

	public List<StatementEntry> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<StatementEntry> transactions) {
		this.transactions = transactions;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
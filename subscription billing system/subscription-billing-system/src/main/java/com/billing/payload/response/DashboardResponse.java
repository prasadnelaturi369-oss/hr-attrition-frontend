package com.billing.payload.response;

import java.util.Map;

import lombok.Data;

@Data
public class DashboardResponse {

	// User stats
	private long totalUsers;
	private long activeUsers;

	// Subscription stats
	private long activeSubscriptions;
	private long expiredSubscriptions;
	private long cancelledSubscriptions;

	// Invoice stats
	private long totalInvoices;
	private long paidInvoices;
	private long pendingInvoices;
	private long overdueInvoices;

	// Payment stats
	private Double totalRevenue;
	private Double monthlyRevenue;

	// Chart data
	private Map<String, Double> revenueByMonth;

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(long activeUsers) {
		this.activeUsers = activeUsers;
	}

	public long getActiveSubscriptions() {
		return activeSubscriptions;
	}

	public void setActiveSubscriptions(long activeSubscriptions) {
		this.activeSubscriptions = activeSubscriptions;
	}

	public long getExpiredSubscriptions() {
		return expiredSubscriptions;
	}

	public void setExpiredSubscriptions(long expiredSubscriptions) {
		this.expiredSubscriptions = expiredSubscriptions;
	}

	public long getCancelledSubscriptions() {
		return cancelledSubscriptions;
	}

	public void setCancelledSubscriptions(long cancelledSubscriptions) {
		this.cancelledSubscriptions = cancelledSubscriptions;
	}

	public long getTotalInvoices() {
		return totalInvoices;
	}

	public void setTotalInvoices(long totalInvoices) {
		this.totalInvoices = totalInvoices;
	}

	public long getPaidInvoices() {
		return paidInvoices;
	}

	public void setPaidInvoices(long paidInvoices) {
		this.paidInvoices = paidInvoices;
	}

	public long getPendingInvoices() {
		return pendingInvoices;
	}

	public void setPendingInvoices(long pendingInvoices) {
		this.pendingInvoices = pendingInvoices;
	}

	public long getOverdueInvoices() {
		return overdueInvoices;
	}

	public void setOverdueInvoices(long overdueInvoices) {
		this.overdueInvoices = overdueInvoices;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public Double getMonthlyRevenue() {
		return monthlyRevenue;
	}

	public void setMonthlyRevenue(Double monthlyRevenue) {
		this.monthlyRevenue = monthlyRevenue;
	}

	public Map<String, Double> getRevenueByMonth() {
		return revenueByMonth;
	}

	public void setRevenueByMonth(Map<String, Double> revenueByMonth) {
		this.revenueByMonth = revenueByMonth;
	}

}
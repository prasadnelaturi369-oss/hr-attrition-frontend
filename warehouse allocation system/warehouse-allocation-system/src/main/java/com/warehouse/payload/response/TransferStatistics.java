package com.warehouse.payload.response;

public class TransferStatistics {
	private long totalTransfers;
	private long completedTransfers;
	private long pendingTransfers;
	private long failedTransfers;
	private long cancelledTransfers;

	public TransferStatistics() {
	}

	public TransferStatistics(long totalTransfers, long completedTransfers, long pendingTransfers, long failedTransfers,
			long cancelledTransfers) {
		this.totalTransfers = totalTransfers;
		this.completedTransfers = completedTransfers;
		this.pendingTransfers = pendingTransfers;
		this.failedTransfers = failedTransfers;
		this.cancelledTransfers = cancelledTransfers;
	}

	public long getTotalTransfers() {
		return totalTransfers;
	}

	public void setTotalTransfers(long totalTransfers) {
		this.totalTransfers = totalTransfers;
	}

	public long getCompletedTransfers() {
		return completedTransfers;
	}

	public void setCompletedTransfers(long completedTransfers) {
		this.completedTransfers = completedTransfers;
	}

	public long getPendingTransfers() {
		return pendingTransfers;
	}

	public void setPendingTransfers(long pendingTransfers) {
		this.pendingTransfers = pendingTransfers;
	}

	public long getFailedTransfers() {
		return failedTransfers;
	}

	public void setFailedTransfers(long failedTransfers) {
		this.failedTransfers = failedTransfers;
	}

	public long getCancelledTransfers() {
		return cancelledTransfers;
	}

	public void setCancelledTransfers(long cancelledTransfers) {
		this.cancelledTransfers = cancelledTransfers;
	}
}
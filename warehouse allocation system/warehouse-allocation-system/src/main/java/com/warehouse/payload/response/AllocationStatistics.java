package com.warehouse.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocationStatistics {
	private long totalAllocations;
	private long allocatedAllocations;
	private long cancelledAllocations;
	private long fulfilledAllocations;

	public AllocationStatistics(long totalAllocations, long allocatedAllocations, long cancelledAllocations,
			long fulfilledAllocations) {
		this.totalAllocations = totalAllocations;
		this.allocatedAllocations = allocatedAllocations;
		this.cancelledAllocations = cancelledAllocations;
		this.fulfilledAllocations = fulfilledAllocations;
	}

	public long getTotalAllocations() {
		return totalAllocations;
	}

	public void setTotalAllocations(long totalAllocations) {
		this.totalAllocations = totalAllocations;
	}

	public long getAllocatedAllocations() {
		return allocatedAllocations;
	}

	public void setAllocatedAllocations(long allocatedAllocations) {
		this.allocatedAllocations = allocatedAllocations;
	}

	public long getCancelledAllocations() {
		return cancelledAllocations;
	}

	public void setCancelledAllocations(long cancelledAllocations) {
		this.cancelledAllocations = cancelledAllocations;
	}

	public long getFulfilledAllocations() {
		return fulfilledAllocations;
	}

	public void setFulfilledAllocations(long fulfilledAllocations) {
		this.fulfilledAllocations = fulfilledAllocations;
	}

}
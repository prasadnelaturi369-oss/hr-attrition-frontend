package com.warehouse.payload.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class TransferSummary {
	private Map<String, StatusSummary> statusSummary = new HashMap<>();

	public void addStatusSummary(String status, Long count, Long totalQuantity) {
		statusSummary.put(status, new StatusSummary(count, totalQuantity));
	}

	@Data
	public static class StatusSummary {
		private Long count;
		private Long totalQuantity;

		public StatusSummary(Long count, Long totalQuantity) {
			this.count = count;
			this.totalQuantity = totalQuantity;
		}

		public StatusSummary() {
		}

		public Long getCount() {
			return count;
		}

		public void setCount(Long count) {
			this.count = count;
		}

		public Long getTotalQuantity() {
			return totalQuantity;
		}

		public void setTotalQuantity(Long totalQuantity) {
			this.totalQuantity = totalQuantity;
		}

	}
}
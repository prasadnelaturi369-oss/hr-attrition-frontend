package com.warehouse.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockTransferRequest {

	@NotNull(message = "Source warehouse ID is required")
	private Long sourceWarehouseId;

	@NotNull(message = "Target warehouse ID is required")
	private Long targetWarehouseId;

	@NotNull(message = "Product ID is required")
	private Long productId;

	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity;

	private String transferType = "MANUAL";

	public Long getSourceWarehouseId() {
		return sourceWarehouseId;
	}

	public void setSourceWarehouseId(Long sourceWarehouseId) {
		this.sourceWarehouseId = sourceWarehouseId;
	}

	public Long getTargetWarehouseId() {
		return targetWarehouseId;
	}

	public void setTargetWarehouseId(Long targetWarehouseId) {
		this.targetWarehouseId = targetWarehouseId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

}

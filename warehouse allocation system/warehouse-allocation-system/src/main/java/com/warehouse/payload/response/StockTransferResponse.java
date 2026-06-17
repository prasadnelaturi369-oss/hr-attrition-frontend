package com.warehouse.payload.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StockTransferResponse {
	private Long transferId;
	private String transferNumber;
	private Long sourceWarehouseId;
	private String sourceWarehouseName;
	private String sourceWarehouseLocation;
	private Long targetWarehouseId;
	private String targetWarehouseName;
	private String targetWarehouseLocation;
	private Long productId;
	private String productName;
	private String productSku;
	private Integer quantity;
	private LocalDateTime transferDate;
	private String status;
	private String transferType;

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public String getTransferNumber() {
		return transferNumber;
	}

	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
	}

	public Long getSourceWarehouseId() {
		return sourceWarehouseId;
	}

	public void setSourceWarehouseId(Long sourceWarehouseId) {
		this.sourceWarehouseId = sourceWarehouseId;
	}

	public String getSourceWarehouseName() {
		return sourceWarehouseName;
	}

	public void setSourceWarehouseName(String sourceWarehouseName) {
		this.sourceWarehouseName = sourceWarehouseName;
	}

	public String getSourceWarehouseLocation() {
		return sourceWarehouseLocation;
	}

	public void setSourceWarehouseLocation(String sourceWarehouseLocation) {
		this.sourceWarehouseLocation = sourceWarehouseLocation;
	}

	public Long getTargetWarehouseId() {
		return targetWarehouseId;
	}

	public void setTargetWarehouseId(Long targetWarehouseId) {
		this.targetWarehouseId = targetWarehouseId;
	}

	public String getTargetWarehouseName() {
		return targetWarehouseName;
	}

	public void setTargetWarehouseName(String targetWarehouseName) {
		this.targetWarehouseName = targetWarehouseName;
	}

	public String getTargetWarehouseLocation() {
		return targetWarehouseLocation;
	}

	public void setTargetWarehouseLocation(String targetWarehouseLocation) {
		this.targetWarehouseLocation = targetWarehouseLocation;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(LocalDateTime transferDate) {
		this.transferDate = transferDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

}
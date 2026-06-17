package com.warehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransfer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "transfer_number", unique = true, nullable = false)
	private String transferNumber;

	@Column(name = "source_warehouse_id", nullable = false)
	private Long sourceWarehouseId;

	@Column(name = "target_warehouse_id", nullable = false)
	private Long targetWarehouseId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "transfer_date", nullable = false)
	private LocalDateTime transferDate;

	@Column(nullable = false)
	private String status;

	@Column(name = "transfer_type")
	private String transferType;

	@PrePersist
	protected void onCreate() {
		transferDate = LocalDateTime.now();
		status = "PENDING";
		transferNumber = "TRF-" + System.currentTimeMillis() + "-" + (long) (Math.random() * 10000);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
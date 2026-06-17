package com.warehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "allocations", indexes = { @Index(name = "idx_product_id", columnList = "product_id"),
		@Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
		@Index(name = "idx_allocated_at", columnList = "allocated_at") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "allocation_number", unique = true, nullable = false)
	private String allocationNumber;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "allocated_at", nullable = false)
	private LocalDateTime allocatedAt;

	@Column(nullable = false)
	private String status;

	@Column(name = "reference_number")
	private String referenceNumber;

	@PrePersist
	protected void onCreate() {
		allocatedAt = LocalDateTime.now();
		status = "ALLOCATED";
		allocationNumber = "ALLOC-" + System.currentTimeMillis() + "-" + (long) (Math.random() * 10000);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAllocationNumber() {
		return allocationNumber;
	}

	public void setAllocationNumber(String allocationNumber) {
		this.allocationNumber = allocationNumber;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getAllocatedAt() {
		return allocatedAt;
	}

	public void setAllocatedAt(LocalDateTime allocatedAt) {
		this.allocatedAt = allocatedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

}
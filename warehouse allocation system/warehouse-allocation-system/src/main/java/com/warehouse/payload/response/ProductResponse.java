package com.warehouse.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

	private Long id;
	private String name;
	private String sku;
	private BigDecimal price;
	private Integer totalStock;
	private String description;
	private String category;
	private String brand;
	private String unitOfMeasure;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Integer warehouseCount;
	private Integer availableQuantity;
	private Integer allocatedQuantity;

	// Default constructor
	public ProductResponse() {
	}

	// Constructor for basic product info
	public ProductResponse(Long id, String name, String sku, BigDecimal price, Integer totalStock,
			LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.totalStock = totalStock;
		this.createdAt = createdAt;
		this.status = totalStock != null && totalStock > 0 ? "IN_STOCK" : "OUT_OF_STOCK";
	}

	// Constructor with inventory details
	public ProductResponse(Long id, String name, String sku, BigDecimal price, Integer totalStock,
			Integer availableQuantity, Integer allocatedQuantity, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.totalStock = totalStock;
		this.availableQuantity = availableQuantity;
		this.allocatedQuantity = allocatedQuantity;
		this.createdAt = createdAt;
		this.status = availableQuantity != null && availableQuantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK";
	}

	// Full constructor
	public ProductResponse(Long id, String name, String sku, BigDecimal price, Integer totalStock, String description,
			String category, String brand, String unitOfMeasure, String status, LocalDateTime createdAt,
			LocalDateTime updatedAt, Integer warehouseCount, Integer availableQuantity, Integer allocatedQuantity) {
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.totalStock = totalStock;
		this.description = description;
		this.category = category;
		this.brand = brand;
		this.unitOfMeasure = unitOfMeasure;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.warehouseCount = warehouseCount;
		this.availableQuantity = availableQuantity;
		this.allocatedQuantity = allocatedQuantity;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getWarehouseCount() {
		return warehouseCount;
	}

	public void setWarehouseCount(Integer warehouseCount) {
		this.warehouseCount = warehouseCount;
	}

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Integer getAllocatedQuantity() {
		return allocatedQuantity;
	}

	public void setAllocatedQuantity(Integer allocatedQuantity) {
		this.allocatedQuantity = allocatedQuantity;
	}
}
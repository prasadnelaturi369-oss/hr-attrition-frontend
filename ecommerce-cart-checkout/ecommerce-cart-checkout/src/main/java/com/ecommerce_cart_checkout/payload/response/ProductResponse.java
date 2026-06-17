package com.ecommerce_cart_checkout.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product response")
public class ProductResponse {

	@Schema(description = "Product ID", example = "1")
	private Long id;

	@Schema(description = "Product name", example = "Apple iPhone 15 Pro")
	private String name;

	@Schema(description = "Product price", example = "129999.99")
	private BigDecimal price;

	@Schema(description = "Stock quantity", example = "25")
	private Integer stockQuantity;

	@Schema(description = "Product status", example = "IN_STOCK")
	private String status;

	@Schema(description = "Creation timestamp")
	private LocalDateTime createdAt;

	public ProductResponse() {
	}

	public ProductResponse(Long id, String name, BigDecimal price, Integer stockQuantity, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.createdAt = createdAt;
		this.status = (stockQuantity != null && stockQuantity > 0) ? "IN_STOCK" : "OUT_OF_STOCK";
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
		this.status = (stockQuantity != null && stockQuantity > 0) ? "IN_STOCK" : "OUT_OF_STOCK";
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
}
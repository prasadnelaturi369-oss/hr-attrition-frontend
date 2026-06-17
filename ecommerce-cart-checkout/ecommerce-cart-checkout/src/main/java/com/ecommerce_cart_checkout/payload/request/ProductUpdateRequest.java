package com.ecommerce_cart_checkout.payload.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class ProductUpdateRequest {

	private String name;

	@DecimalMin(value = "0.01", message = "Price must be greater than 0")
	private BigDecimal price;

	@Min(value = 0, message = "Stock quantity cannot be negative")
	private Integer stockQuantity;

	public ProductUpdateRequest() {
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
	}
}
package com.ecommerce_cart_checkout.payload.request;

import jakarta.validation.constraints.NotNull;

public class CheckoutRequest {
	@NotNull(message = "User ID is required")
	private Long userId;

	private String couponCode;

	public CheckoutRequest() {
	}

	public CheckoutRequest(Long userId, String couponCode) {
		this.userId = userId;
		this.couponCode = couponCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
}

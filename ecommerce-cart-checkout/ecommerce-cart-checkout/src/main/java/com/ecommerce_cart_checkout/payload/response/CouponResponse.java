package com.ecommerce_cart_checkout.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponResponse {

	private Long id;

	private String code;

	private String discountType;

	private BigDecimal discountValue;

	private LocalDateTime expiryDate;

	private Boolean isActive;

	private BigDecimal minimumOrderAmount;

	private BigDecimal maxDiscountAmount;

	public CouponResponse() {
	}

	public CouponResponse(Long id, String code, String discountType, BigDecimal discountValue, LocalDateTime expiryDate,
			Boolean isActive, BigDecimal minimumOrderAmount, BigDecimal maxDiscountAmount) {

		this.id = id;
		this.code = code;
		this.discountType = discountType;
		this.discountValue = discountValue;
		this.expiryDate = expiryDate;
		this.isActive = isActive;
		this.minimumOrderAmount = minimumOrderAmount;
		this.maxDiscountAmount = maxDiscountAmount;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getDiscountType() {
		return discountType;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public BigDecimal getMinimumOrderAmount() {
		return minimumOrderAmount;
	}

	public BigDecimal getMaxDiscountAmount() {
		return maxDiscountAmount;
	}
}
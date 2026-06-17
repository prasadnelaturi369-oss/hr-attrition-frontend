package com.ecommerce_cart_checkout.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ecommerce_cart_checkout.entity.Coupon;
import com.ecommerce_cart_checkout.exception.BusinessException;
import com.ecommerce_cart_checkout.payload.request.CouponRequest;
import com.ecommerce_cart_checkout.payload.response.CouponResponse;
import com.ecommerce_cart_checkout.repository.CouponRepository;

@Service
public class CouponService {
	private final CouponRepository couponRepository;

	public CouponService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	public CouponResponse createCoupon(CouponRequest request) {

		if (couponRepository.findByCodeAndIsActiveTrue(request.getCode()).isPresent()) {

			throw new BusinessException("Coupon already exists with code: " + request.getCode());
		}

		Coupon coupon = new Coupon();

		coupon.setCode(request.getCode());
		coupon.setDiscountType(request.getDiscountType());
		coupon.setDiscountValue(request.getDiscountValue());
		coupon.setExpiryDate(request.getExpiryDate());
		coupon.setIsActive(request.getIsActive());
		coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
		coupon.setMaxDiscountAmount(request.getMaxDiscountAmount());

		Coupon savedCoupon = couponRepository.save(coupon);

		return new CouponResponse(savedCoupon.getId(), savedCoupon.getCode(), savedCoupon.getDiscountType(),
				savedCoupon.getDiscountValue(), savedCoupon.getExpiryDate(), savedCoupon.getIsActive(),
				savedCoupon.getMinimumOrderAmount(), savedCoupon.getMaxDiscountAmount());
	}

	public BigDecimal applyCoupon(String couponCode, BigDecimal subtotal) {
		Coupon coupon = couponRepository.findValidCoupon(couponCode, LocalDateTime.now())
				.orElseThrow(() -> new BusinessException("Invalid or expired coupon code: " + couponCode));

		if (coupon.getMinimumOrderAmount() != null && subtotal.compareTo(coupon.getMinimumOrderAmount()) < 0) {
			throw new BusinessException(
					"Minimum order amount of " + coupon.getMinimumOrderAmount() + " required for this coupon");
		}

		BigDecimal discount = BigDecimal.ZERO;

		if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
			discount = subtotal.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
			// Apply max discount limit if exists
			if (coupon.getMaxDiscountAmount() != null && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
				discount = coupon.getMaxDiscountAmount();
			}
		} else if ("FLAT".equalsIgnoreCase(coupon.getDiscountType())) {
			discount = coupon.getDiscountValue();
			if (discount.compareTo(subtotal) > 0) {
				discount = subtotal;
			}
		}

		return discount;
	}

	public Coupon getCoupon(String code) {
		return couponRepository.findValidCoupon(code, LocalDateTime.now())
				.orElseThrow(() -> new BusinessException("Coupon not found or expired"));
	}
}
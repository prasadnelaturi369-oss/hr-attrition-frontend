package com.ecommerce_cart_checkout.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce_cart_checkout.payload.request.CouponRequest;
import com.ecommerce_cart_checkout.payload.response.CouponResponse;
import com.ecommerce_cart_checkout.service.CouponService;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

	private final CouponService couponService;

	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@PostMapping
	public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest request) {

		CouponResponse response = couponService.createCoupon(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
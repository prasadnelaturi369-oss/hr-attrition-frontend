package com.ecommerce_cart_checkout.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce_cart_checkout.payload.request.CartRequest;
import com.ecommerce_cart_checkout.payload.response.CartResponse;
import com.ecommerce_cart_checkout.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/add")
	public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(request));
	}

	@PutMapping("/update/{userId}/{productId}")
	public ResponseEntity<CartResponse> updateQuantity(@PathVariable Long userId, @PathVariable Long productId,
			@RequestParam Integer quantity) {
		return ResponseEntity.ok(cartService.updateCartItemQuantity(userId, productId, quantity));
	}

	@DeleteMapping("/remove/{userId}/{productId}")
	public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
		cartService.removeFromCart(userId, productId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{userId}")
	public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
		return ResponseEntity.ok(cartService.getCart(userId));
	}
}
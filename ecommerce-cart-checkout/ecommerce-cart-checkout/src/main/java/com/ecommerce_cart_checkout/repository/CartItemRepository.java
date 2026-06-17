package com.ecommerce_cart_checkout.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce_cart_checkout.entity.Cart;
import com.ecommerce_cart_checkout.entity.CartItem;
import com.ecommerce_cart_checkout.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

	void deleteByCartAndProduct(Cart cart, Product product);
}
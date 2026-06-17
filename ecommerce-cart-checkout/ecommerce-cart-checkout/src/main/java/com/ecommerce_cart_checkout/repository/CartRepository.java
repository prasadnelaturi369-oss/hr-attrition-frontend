package com.ecommerce_cart_checkout.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce_cart_checkout.entity.Cart;
import com.ecommerce_cart_checkout.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUser(User user);
}
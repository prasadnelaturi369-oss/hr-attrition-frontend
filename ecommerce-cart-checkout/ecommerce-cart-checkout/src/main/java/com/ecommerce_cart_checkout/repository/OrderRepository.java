package com.ecommerce_cart_checkout.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce_cart_checkout.entity.Order;
import com.ecommerce_cart_checkout.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findByUser(User user, Pageable pageable);

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId")
	Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
	Page<Order> findOrderHistoryByUserId(@Param("userId") Long userId, Pageable pageable);
}
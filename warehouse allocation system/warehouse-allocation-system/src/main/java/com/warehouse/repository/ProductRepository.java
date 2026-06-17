package com.warehouse.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findBySku(String sku);

	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
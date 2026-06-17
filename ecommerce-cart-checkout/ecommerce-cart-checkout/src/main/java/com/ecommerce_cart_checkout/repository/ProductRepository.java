package com.ecommerce_cart_checkout.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_cart_checkout.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByName(String name);

	List<Product> findByNameContainingIgnoreCase(String name);

	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	List<Product> findByStockQuantityLessThan(Integer threshold);

	@Query("SELECT p FROM Product p WHERE p.stockQuantity > 0") 
	List<Product> findInStockProducts();

	@Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity <= :threshold")
	long countLowStockProducts(@Param("threshold") Integer threshold);

	List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

	Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.price = :price WHERE p.id = :productId")
	int updateProductPrice(@Param("productId") Long productId, @Param("price") BigDecimal price);

	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :productId")
	int addStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.id = :productId AND p.stockQuantity >= :quantity")
	int reduceStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :productId")
	int restoreStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
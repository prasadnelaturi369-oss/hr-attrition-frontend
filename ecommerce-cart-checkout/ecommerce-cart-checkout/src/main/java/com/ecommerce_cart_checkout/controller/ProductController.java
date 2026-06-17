package com.ecommerce_cart_checkout.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce_cart_checkout.payload.request.ProductRequest;
import com.ecommerce_cart_checkout.payload.request.ProductStockUpdateRequest;
import com.ecommerce_cart_checkout.payload.request.ProductUpdateRequest;
import com.ecommerce_cart_checkout.payload.response.ProductResponse;
import com.ecommerce_cart_checkout.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		ProductResponse response = productService.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		ProductResponse response = productService.getProductById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		Page<ProductResponse> products = productService.getAllProducts(page, size, sortBy, sortDir);
		return ResponseEntity.ok(products);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductUpdateRequest request) {

		ProductResponse response = productService.updateProduct(id, request);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/stock")
	public ResponseEntity<ProductResponse> updateProductStock(@PathVariable Long id,
			@Valid @RequestBody ProductStockUpdateRequest request) {

		ProductResponse response = productService.updateProductStock(id, request);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/price")
	public ResponseEntity<ProductResponse> updateProductPrice(@PathVariable Long id, @RequestParam BigDecimal price) {

		ProductResponse response = productService.updateProductPrice(id, price);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<Page<ProductResponse>> searchProducts(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<ProductResponse> products = productService.searchProducts(name, page, size);
		return ResponseEntity.ok(products);
	}

	@GetMapping("/price-range")
	public ResponseEntity<Page<ProductResponse>> getProductsByPriceRange(@RequestParam BigDecimal minPrice,
			@RequestParam BigDecimal maxPrice, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice, page, size);
		return ResponseEntity.ok(products);
	}

	@GetMapping("/low-stock")
	public ResponseEntity<List<ProductResponse>> getLowStockProducts(
			@RequestParam(defaultValue = "10") Integer threshold) {

		List<ProductResponse> products = productService.getLowStockProducts(threshold);
		return ResponseEntity.ok(products);
	}

	@GetMapping("/in-stock")
	public ResponseEntity<List<ProductResponse>> getInStockProducts() {
		List<ProductResponse> products = productService.getInStockProducts();
		return ResponseEntity.ok(products);
	}

	@PostMapping("/{id}/bulk-stock")
	public ResponseEntity<Void> bulkAddStock(@PathVariable Long id, @RequestParam Integer quantity) {

		productService.bulkAddStock(id, quantity);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/statistics")
	public ResponseEntity<ProductService.ProductStatistics> getProductStatistics() {
		ProductService.ProductStatistics statistics = productService.getProductStatistics();
		return ResponseEntity.ok(statistics);
	}
}
package com.warehouse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.warehouse.payload.request.ProductRequest;
import com.warehouse.payload.response.ProductResponse;
import com.warehouse.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	@Operation(summary = "Create product", description = "Create a new product")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		log.info("REST request to create product: {}", request.getName());
		ProductResponse response = productService.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		log.info("REST request to get product with id: {}", id);
		ProductResponse response = productService.getProductById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@Operation(summary = "Get all products", description = "Retrieve all products with pagination")
	public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));

		Page<ProductResponse> products = productService.getAllProducts(pageable);
		return ResponseEntity.ok(products);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update product", description = "Update an existing product")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {
		log.info("REST request to update product with id: {}", id);
		ProductResponse response = productService.updateProduct(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete product", description = "Delete a product")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		log.info("REST request to delete product with id: {}", id);
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	@Operation(summary = "Search products", description = "Search products by name")
	public ResponseEntity<Page<ProductResponse>> searchProducts(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<ProductResponse> products = productService.searchProducts(name, pageable);
		return ResponseEntity.ok(products);
	}
}
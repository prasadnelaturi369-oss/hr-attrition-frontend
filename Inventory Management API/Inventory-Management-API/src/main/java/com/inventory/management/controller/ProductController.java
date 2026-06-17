package com.inventory.management.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.management.payload.request.ProductRequest;
import com.inventory.management.payload.response.ProductResponse;
import com.inventory.management.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService service;
	
	public ProductController(ProductService service) {
        this.service = service;
    }

	@PostMapping
	public ProductResponse add(@Validated @RequestBody ProductRequest request) {
		return service.addProduct(request);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Long id, @Validated @RequestBody ProductRequest request) {
		return service.updateProduct(id, request);
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable Long id) {
		return service.getProduct(id);
	}

	@GetMapping
	public List<ProductResponse> getAll() {
		return service.getAllProducts();
	}

	@PatchMapping("/{id}/increase")
	public ProductResponse increase(@PathVariable Long id, @RequestParam int quantity) {
		return service.increaseStock(id, quantity);
	}

	@PatchMapping("/{id}/decrease")
	public ProductResponse decrease(@PathVariable Long id, @RequestParam int quantity) {
		return service.decreaseStock(id, quantity);
	}

	@GetMapping("/low-stock")
	public List<ProductResponse> lowStock() {
		return service.getLowStockProducts();
	}
}
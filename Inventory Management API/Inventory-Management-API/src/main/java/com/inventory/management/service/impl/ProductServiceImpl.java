package com.inventory.management.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inventory.management.entity.Product;
import com.inventory.management.payload.request.ProductRequest;
import com.inventory.management.payload.response.ProductResponse;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;

	// ✅ Constructor Injection FIX
	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public ProductResponse addProduct(ProductRequest request) {

		repository.findByName(request.getName()).ifPresent(p -> {
			throw new RuntimeException("Product already exists");
		});

		Product product = new Product();
		product.setName(request.getName());
		product.setCategory(request.getCategory());
		product.setPrice(request.getPrice());
		product.setQuantity(request.getQuantity());

		return mapToResponse(repository.save(product));
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {

		Product product = getEntity(id);

		product.setName(request.getName());
		product.setCategory(request.getCategory());
		product.setPrice(request.getPrice());

		return mapToResponse(repository.save(product));
	}

	@Override
	public ProductResponse getProduct(Long id) {
		return mapToResponse(getEntity(id));
	}

	@Override
	public List<ProductResponse> getAllProducts() {
		return repository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public ProductResponse increaseStock(Long id, int qty) {
		Product product = getEntity(id);
		product.setQuantity(product.getQuantity() + qty);
		return mapToResponse(repository.save(product));
	}

	@Override
	public ProductResponse decreaseStock(Long id, int qty) {

		Product product = getEntity(id);

		if (product.getQuantity() < qty) {
			throw new IllegalArgumentException("Stock cannot go below zero");
		}

		product.setQuantity(product.getQuantity() - qty);
		return mapToResponse(repository.save(product));
	}

	@Override
	public List<ProductResponse> getLowStockProducts() {
		return repository.findAll().stream().filter(p -> p.getQuantity() < 5).map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private Product getEntity(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	private ProductResponse mapToResponse(Product product) {

		ProductResponse response = new ProductResponse();

		response.setId(product.getId());
		response.setName(product.getName());
		response.setCategory(product.getCategory());
		response.setPrice(product.getPrice());
		response.setQuantity(product.getQuantity());

		return response;
	}
}
package com.warehouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.entity.Product;
import com.warehouse.exception.BusinessException;
import com.warehouse.exception.ResourceNotFoundException;
import com.warehouse.payload.request.ProductRequest;
import com.warehouse.payload.response.ProductResponse;
import com.warehouse.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse createProduct(ProductRequest request) {
		log.info("Creating new product: {}", request.getName());

		if (productRepository.findBySku(request.getSku()).isPresent()) {
			throw new BusinessException("Product with SKU '" + request.getSku() + "' already exists");
		}

		Product product = new Product();
		product.setName(request.getName());
		product.setSku(request.getSku());
		product.setPrice(request.getPrice());
		product.setTotalStock(0);

		Product savedProduct = productRepository.save(product);
		return mapToResponse(savedProduct);
	}

	public ProductResponse getProductById(Long id) {
		log.info("Fetching product with id: {}", id);
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		return mapToResponse(product);
	}

	public Page<ProductResponse> getAllProducts(Pageable pageable) {
		log.info("Fetching all products with pagination");
		return productRepository.findAll(pageable).map(this::mapToResponse);
	}

	@Transactional
	public ProductResponse updateProduct(Long id, ProductRequest request) {
		log.info("Updating product with id: {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		if (request.getName() != null) {
			product.setName(request.getName());
		}

		if (request.getSku() != null && !request.getSku().equals(product.getSku())) {
			if (productRepository.findBySku(request.getSku()).isPresent()) {
				throw new BusinessException("Product with SKU '" + request.getSku() + "' already exists");
			}
			product.setSku(request.getSku());
		}

		if (request.getPrice() != null) {
			product.setPrice(request.getPrice());
		}

		Product updatedProduct = productRepository.save(product);
		return mapToResponse(updatedProduct);
	}

	@Transactional
	public void deleteProduct(Long id) {
		log.info("Deleting product with id: {}", id);

		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		}

		productRepository.deleteById(id);
	}

	public Page<ProductResponse> searchProducts(String name, Pageable pageable) {
		log.info("Searching products with name containing: {}", name);
		return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::mapToResponse);
	}

	private ProductResponse mapToResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setSku(product.getSku());
		response.setPrice(product.getPrice());
		response.setTotalStock(product.getTotalStock());
		response.setCreatedAt(product.getCreatedAt());
		return response;
	}
}
package com.ecommerce_cart_checkout.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_cart_checkout.entity.Product;
import com.ecommerce_cart_checkout.exception.BusinessException;
import com.ecommerce_cart_checkout.exception.ResourceNotFoundException;
import com.ecommerce_cart_checkout.payload.request.ProductRequest;
import com.ecommerce_cart_checkout.payload.request.ProductStockUpdateRequest;
import com.ecommerce_cart_checkout.payload.request.ProductUpdateRequest;
import com.ecommerce_cart_checkout.payload.response.ProductResponse;
import com.ecommerce_cart_checkout.repository.ProductRepository;

@Service
public class ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse createProduct(ProductRequest request) {
		log.info("Creating new product: {}", request.getName());

		if (productRepository.findByName(request.getName()).isPresent()) {
			throw new BusinessException("Product with name '" + request.getName() + "' already exists");
		}

		Product product = new Product();
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setStockQuantity(request.getStockQuantity());

		Product savedProduct = productRepository.save(product);
		log.info("Product created successfully with id: {}", savedProduct.getId());

		return convertToResponse(savedProduct);
	}

	private ProductResponse convertToResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity(), // Maps
																														// to
																														// stock_quantity
																														// column
				product.getCreatedAt());
	}

	public ProductResponse getProductById(Long id) {
		log.info("Fetching product with id: {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		return convertToResponse(product);
	}

	public Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
		log.info("Fetching all products - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Product> products = productRepository.findAll(pageable);

		return products.map(this::convertToResponse);
	}

	@Transactional
	public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
		log.info("Updating product with id: {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		if (request.getName() != null && !request.getName().isEmpty()) {
			if (!request.getName().equals(product.getName())
					&& productRepository.findByName(request.getName()).isPresent()) {
				throw new BusinessException("Product with name '" + request.getName() + "' already exists");
			}
			product.setName(request.getName());
		}

		if (request.getPrice() != null) {
			product.setPrice(request.getPrice());
		}

		if (request.getStockQuantity() != null) {
			product.setStockQuantity(request.getStockQuantity());
		}

		Product updatedProduct = productRepository.save(product);
		log.info("Product updated successfully: {}", updatedProduct.getId());

		return convertToResponse(updatedProduct);
	}

	@Transactional
	public ProductResponse updateProductStock(Long id, ProductStockUpdateRequest request) {
		log.info("Updating stock for product: {}, quantity: {}", id, request.getQuantity());

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		int newStock = product.getStockQuantity() + request.getQuantity();
		if (newStock < 0) {
			throw new BusinessException("Insufficient stock. Current stock: " + product.getStockQuantity()
					+ ", Requested reduction: " + Math.abs(request.getQuantity()));
		}

		product.setStockQuantity(newStock);
		Product updatedProduct = productRepository.save(product);
		log.info("Stock updated for product: {}, new stock: {}", id, newStock);

		return convertToResponse(updatedProduct);
	}

	@Transactional
	public void deleteProduct(Long id) {
		log.info("Deleting product with id: {}", id);

		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		}

		productRepository.deleteById(id);
		log.info("Product deleted successfully: {}", id);
	}

	public Page<ProductResponse> searchProducts(String name, int page, int size) {
		log.info("Searching products with name containing: {}", name);

		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);

		return products.map(this::convertToResponse);
	}

	public Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
		log.info("Fetching products with price between {} and {}", minPrice, maxPrice);

		Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());
		Page<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);

		return products.map(this::convertToResponse);
	}

	public List<ProductResponse> getLowStockProducts(Integer threshold) {
		log.info("Fetching products with stock below: {}", threshold);

		List<Product> products = productRepository.findByStockQuantityLessThan(threshold);
		return products.stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	public List<ProductResponse> getInStockProducts() {
		log.info("Fetching all in-stock products");

		List<Product> products = productRepository.findInStockProducts();
		return products.stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	@Transactional
	public ProductResponse updateProductPrice(Long id, BigDecimal price) {
		log.info("Updating price for product: {} to {}", id, price);

		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		}

		int updated = productRepository.updateProductPrice(id, price);
		if (updated == 0) {
			throw new BusinessException("Failed to update product price");
		}

		Product product = productRepository.findById(id).get();
		return convertToResponse(product);
	}

	@Transactional
	public void bulkAddStock(Long id, Integer quantity) {
		log.info("Bulk adding stock for product: {}, quantity: {}", id, quantity);

		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		}

		int updated = productRepository.addStock(id, quantity);
		if (updated == 0) {
			throw new BusinessException("Failed to add stock");
		}
	}

	// Get product statistics
	public ProductStatistics getProductStatistics() {
		log.info("Fetching product statistics");

		long totalProducts = productRepository.count();
		long inStockProducts = productRepository.findInStockProducts().size();
		long lowStockProducts = productRepository.countLowStockProducts(10);

		return new ProductStatistics(totalProducts, inStockProducts, lowStockProducts);
	}

	public static class ProductStatistics {
		private final long totalProducts;
		private final long inStockProducts;
		private final long lowStockProducts;

		public ProductStatistics(long totalProducts, long inStockProducts, long lowStockProducts) {
			this.totalProducts = totalProducts;
			this.inStockProducts = inStockProducts;
			this.lowStockProducts = lowStockProducts;
		}

		public long getTotalProducts() {
			return totalProducts;
		}

		public long getInStockProducts() {
			return inStockProducts;
		}

		public long getLowStockProducts() {
			return lowStockProducts;
		}

		public double getInStockPercentage() {
			return totalProducts > 0 ? (double) inStockProducts / totalProducts * 100 : 0;
		}
	}
}
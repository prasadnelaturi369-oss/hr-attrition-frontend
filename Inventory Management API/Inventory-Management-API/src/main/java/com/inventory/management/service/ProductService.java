package com.inventory.management.service;

import java.util.List;

import com.inventory.management.payload.request.ProductRequest;
import com.inventory.management.payload.response.ProductResponse;

public interface ProductService {

	ProductResponse addProduct(ProductRequest request);

	ProductResponse updateProduct(Long id, ProductRequest request);

	ProductResponse getProduct(Long id);

	List<ProductResponse> getAllProducts();

	ProductResponse increaseStock(Long id, int quantity);

	ProductResponse decreaseStock(Long id, int quantity);

	List<ProductResponse> getLowStockProducts();
}

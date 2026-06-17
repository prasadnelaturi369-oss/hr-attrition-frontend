package com.warehouse.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.entity.Allocation;
import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.exception.BusinessException;
import com.warehouse.exception.InsufficientStockException;
import com.warehouse.exception.ResourceNotFoundException;
import com.warehouse.payload.request.AllocationRequest;
import com.warehouse.payload.response.AllocationResponse;
import com.warehouse.payload.response.AllocationStatistics;
import com.warehouse.repository.AllocationRepository;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.WarehouseInventoryRepository;
import com.warehouse.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllocationService {

	private static final Logger log = LoggerFactory.getLogger(AllocationService.class);

	private final AllocationRepository allocationRepository;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;
	private final WarehouseInventoryRepository inventoryRepository;

	public AllocationService(AllocationRepository allocationRepository, WarehouseRepository warehouseRepository,
			ProductRepository productRepository, WarehouseInventoryRepository inventoryRepository) {
		this.allocationRepository = allocationRepository;
		this.warehouseRepository = warehouseRepository;
		this.productRepository = productRepository;
		this.inventoryRepository = inventoryRepository;
	}

	@Transactional
	public AllocationResponse allocateStock(AllocationRequest request) {
		long startTime = System.currentTimeMillis();

		log.info("Processing allocation request for productId: {}, quantity: {}", request.getProductId(),
				request.getQuantity());

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

		Warehouse warehouse = selectOptimalWarehouse(product, request.getQuantity(), request.getPreferredWarehouseId());

		WarehouseInventory inventory = inventoryRepository.findByWarehouseAndProductWithLock(warehouse, product)
				.orElseThrow(
						() -> new InsufficientStockException("No inventory found for product in selected warehouse"));

		if (inventory.getAvailableQuantity() < request.getQuantity()) {
			throw new InsufficientStockException(
					String.format("Insufficient stock in warehouse %s. Available: %d, Requested: %d",
							warehouse.getName(), inventory.getAvailableQuantity(), request.getQuantity()));
		}

		int updated = inventoryRepository.allocateStock(inventory.getId(), request.getQuantity());
		if (updated == 0) {
			throw new BusinessException("Failed to update inventory due to concurrent modification");
		}

		Allocation allocation = new Allocation();
		allocation.setProduct(product);
		allocation.setWarehouse(warehouse);
		allocation.setQuantity(request.getQuantity());
		allocation.setReferenceNumber(request.getReferenceNumber());

		allocation = allocationRepository.save(allocation);

		long endTime = System.currentTimeMillis();
		log.info("Allocation completed successfully in {} ms", (endTime - startTime));

		return mapToResponse(allocation);
	}

	private Warehouse selectOptimalWarehouse(Product product, Integer quantity, Long preferredWarehouseId) {
		if (preferredWarehouseId != null) {
			Warehouse preferred = warehouseRepository.findById(preferredWarehouseId)
					.orElseThrow(() -> new ResourceNotFoundException("Preferred warehouse not found"));

			if ("ACTIVE".equals(preferred.getStatus())) {
				WarehouseInventory inventory = inventoryRepository.findByWarehouseAndProduct(preferred, product)
						.orElse(null);

				if (inventory != null && inventory.getAvailableQuantity() >= quantity) {
					return preferred;
				}
			}
		}

		List<WarehouseInventory> availableInventories = inventoryRepository.findAvailableWarehousesForProduct(product);

		for (WarehouseInventory inv : availableInventories) {
			if (inv.getAvailableQuantity() >= quantity && "ACTIVE".equals(inv.getWarehouse().getStatus())) {
				return inv.getWarehouse();
			}
		}

		throw new InsufficientStockException(
				"No warehouse with sufficient stock found for product: " + product.getName());
	}

	public AllocationResponse getAllocationById(Long allocationId) {
		log.info("Fetching allocation with id: {}", allocationId);

		Allocation allocation = allocationRepository.findById(allocationId)
				.orElseThrow(() -> new ResourceNotFoundException("Allocation not found with id: " + allocationId));

		return mapToResponse(allocation);
	}

	@Transactional
	public void cancelAllocation(Long allocationId) {
		Allocation allocation = allocationRepository.findById(allocationId)
				.orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

		if (!"ALLOCATED".equals(allocation.getStatus())) {
			throw new BusinessException(
					"Only allocated allocations can be cancelled. Current status: " + allocation.getStatus());
		}

		WarehouseInventory inventory = inventoryRepository
				.findByWarehouseAndProduct(allocation.getWarehouse(), allocation.getProduct())
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

		int updated = inventoryRepository.cancelAllocation(inventory.getId(), allocation.getQuantity());
		if (updated == 0) {
			throw new BusinessException("Failed to cancel allocation due to concurrent modification");
		}

		allocation.setStatus("CANCELLED");
		allocationRepository.save(allocation);

		log.info("Allocation {} cancelled successfully", allocationId);
	}

	public Page<AllocationResponse> getAllAllocations(Pageable pageable) {
		log.info("Fetching all allocations with pagination");
		return allocationRepository.findAll(pageable).map(this::mapToResponse);
	}

	public Page<AllocationResponse> searchAllocations(Long productId, Long warehouseId, String status,
			LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
		log.info("Searching allocations with filters");

		Page<Allocation> allocations = allocationRepository.searchAllocations(productId, warehouseId, status, startDate,
				endDate, pageable);
		return allocations.map(this::mapToResponse);
	}

	public Page<AllocationResponse> getAllocationsByProduct(Long productId, Pageable pageable) {
		log.info("Fetching allocations for product: {}", productId);

		if (!productRepository.existsById(productId)) {
			throw new ResourceNotFoundException("Product not found with id: " + productId);
		}

		return allocationRepository.findByProductId(productId, pageable).map(this::mapToResponse);
	}

	public Page<AllocationResponse> getAllocationsByWarehouse(Long warehouseId, Pageable pageable) {
		log.info("Fetching allocations for warehouse: {}", warehouseId);

		if (!warehouseRepository.existsById(warehouseId)) {
			throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
		}

		return allocationRepository.findByWarehouseId(warehouseId, pageable).map(this::mapToResponse);
	}

	public AllocationStatistics getAllocationStatistics() {
		log.info("Fetching allocation statistics");

		long totalAllocations = allocationRepository.count();
		long allocatedAllocations = allocationRepository.countByStatus("ALLOCATED");
		long cancelledAllocations = allocationRepository.countByStatus("CANCELLED");
		long fulfilledAllocations = allocationRepository.countByStatus("FULFILLED");

		return new AllocationStatistics(totalAllocations, allocatedAllocations, cancelledAllocations,
				fulfilledAllocations);
	}

	public List<Object[]> getAllocationSummaryByProduct() {
		log.info("Fetching allocation summary by product");
		return allocationRepository.getAllocationSummaryByProduct();
	}

	public List<Object[]> getAllocationSummaryByWarehouse() {
		log.info("Fetching allocation summary by warehouse");
		return allocationRepository.getAllocationSummaryByWarehouse();
	}

	private AllocationResponse mapToResponse(Allocation allocation) {
		AllocationResponse response = new AllocationResponse();
		response.setAllocationId(allocation.getId());
		response.setAllocationNumber(allocation.getAllocationNumber());
		response.setProductId(allocation.getProduct().getId());
		response.setProductName(allocation.getProduct().getName());
		response.setProductSku(allocation.getProduct().getSku());
		response.setWarehouseId(allocation.getWarehouse().getId());
		response.setWarehouseName(allocation.getWarehouse().getName());
		response.setWarehouseLocation(allocation.getWarehouse().getLocation());
		response.setQuantity(allocation.getQuantity());
		response.setAllocatedAt(allocation.getAllocatedAt());
		response.setStatus(allocation.getStatus());
		response.setReferenceNumber(allocation.getReferenceNumber());
		return response;
	}
}
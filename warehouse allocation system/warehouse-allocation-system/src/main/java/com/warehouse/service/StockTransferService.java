package com.warehouse.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.entity.Product;
import com.warehouse.entity.StockTransfer;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.exception.BusinessException;
import com.warehouse.exception.InsufficientStockException;
import com.warehouse.exception.ResourceNotFoundException;
import com.warehouse.payload.request.StockTransferRequest;
import com.warehouse.payload.response.StockTransferResponse;
import com.warehouse.payload.response.TransferStatistics;
import com.warehouse.payload.response.TransferSummary;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.StockTransferRepository;
import com.warehouse.repository.WarehouseInventoryRepository;
import com.warehouse.repository.WarehouseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockTransferService {

	private static final Logger log = LoggerFactory.getLogger(StockTransferService.class);

	private final StockTransferRepository transferRepository;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;
	private final WarehouseInventoryRepository inventoryRepository;

	public StockTransferService(StockTransferRepository transferRepository, WarehouseRepository warehouseRepository,
			ProductRepository productRepository, WarehouseInventoryRepository inventoryRepository) {
		this.transferRepository = transferRepository;
		this.warehouseRepository = warehouseRepository;
		this.productRepository = productRepository;
		this.inventoryRepository = inventoryRepository;
	}

	@Transactional
	public StockTransferResponse transferStock(StockTransferRequest request) {
		log.info("Processing stock transfer from warehouse {} to {}", request.getSourceWarehouseId(),
				request.getTargetWarehouseId());

		Warehouse sourceWarehouse = warehouseRepository.findById(request.getSourceWarehouseId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Source warehouse not found with id: " + request.getSourceWarehouseId()));

		Warehouse targetWarehouse = warehouseRepository.findById(request.getTargetWarehouseId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Target warehouse not found with id: " + request.getTargetWarehouseId()));

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

		WarehouseInventory sourceInventory = inventoryRepository
				.findByWarehouseAndProductWithLock(sourceWarehouse, product)
				.orElseThrow(() -> new InsufficientStockException("Product not found in source warehouse"));

		if (sourceInventory.getAvailableQuantity() < request.getQuantity()) {
			throw new InsufficientStockException(
					String.format("Insufficient stock in source warehouse '%s'. Available: %d, Requested: %d",
							sourceWarehouse.getName(), sourceInventory.getAvailableQuantity(), request.getQuantity()));
		}

		int targetCurrentStock = inventoryRepository.findByWarehouseAndProduct(targetWarehouse, product)
				.map(WarehouseInventory::getAvailableQuantity).orElse(0);

		if (targetCurrentStock + request.getQuantity() > targetWarehouse.getCapacity()) {
			throw new BusinessException(
					String.format("Target warehouse '%s' capacity exceeded. Current: %d, Capacity: %d, Transfer: %d",
							targetWarehouse.getName(), targetCurrentStock, targetWarehouse.getCapacity(),
							request.getQuantity()));
		}

		StockTransfer transfer = new StockTransfer();
		transfer.setSourceWarehouseId(request.getSourceWarehouseId());
		transfer.setTargetWarehouseId(request.getTargetWarehouseId());
		transfer.setProductId(request.getProductId());
		transfer.setQuantity(request.getQuantity());
		transfer.setTransferType(request.getTransferType() != null ? request.getTransferType() : "MANUAL");
		transfer.setStatus("PENDING");

		transfer = transferRepository.save(transfer);

		try {
			int sourceUpdated = inventoryRepository.allocateStock(sourceInventory.getId(), request.getQuantity());
			if (sourceUpdated == 0) {
				throw new BusinessException("Failed to update source inventory due to concurrent modification");
			}

			WarehouseInventory targetInventory = inventoryRepository.findByWarehouseAndProduct(targetWarehouse, product)
					.orElse(null);

			if (targetInventory == null) {
				targetInventory = new WarehouseInventory();
				targetInventory.setWarehouse(targetWarehouse);
				targetInventory.setProduct(product);
				targetInventory.setAvailableQuantity(request.getQuantity());
				targetInventory.setAllocatedQuantity(0);
				inventoryRepository.save(targetInventory);
			} else {
				int targetUpdated = inventoryRepository.addStock(targetInventory.getId(), request.getQuantity());
				if (targetUpdated == 0) {
					inventoryRepository.addStock(sourceInventory.getId(), request.getQuantity());
					throw new BusinessException("Failed to update target inventory due to concurrent modification");
				}
			}

			transfer.setStatus("COMPLETED");
			transfer = transferRepository.save(transfer);

			log.info("Stock transfer completed successfully. Transfer ID: {}, Number: {}", transfer.getId(),
					transfer.getTransferNumber());

		} catch (Exception e) {
			transfer.setStatus("FAILED");
			transferRepository.save(transfer);
			log.error("Stock transfer failed: {}", e.getMessage(), e);
			throw e;
		}

		return mapToResponse(transfer, sourceWarehouse, targetWarehouse, product);
	}

	public StockTransferResponse getTransferById(Long transferId) {
		log.info("Fetching stock transfer with id: {}", transferId);

		StockTransfer transfer = transferRepository.findById(transferId)
				.orElseThrow(() -> new ResourceNotFoundException("Stock transfer not found with id: " + transferId));

		Warehouse sourceWarehouse = warehouseRepository.findById(transfer.getSourceWarehouseId())
				.orElseThrow(() -> new ResourceNotFoundException("Source warehouse not found"));

		Warehouse targetWarehouse = warehouseRepository.findById(transfer.getTargetWarehouseId())
				.orElseThrow(() -> new ResourceNotFoundException("Target warehouse not found"));

		Product product = productRepository.findById(transfer.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		return mapToResponse(transfer, sourceWarehouse, targetWarehouse, product);
	}

	public Page<StockTransferResponse> getAllTransfers(Pageable pageable) {
		log.info("Fetching all stock transfers with pagination");
		return transferRepository.findAll(pageable).map(transfer -> {
			Warehouse source = warehouseRepository.findById(transfer.getSourceWarehouseId()).orElse(null);
			Warehouse target = warehouseRepository.findById(transfer.getTargetWarehouseId()).orElse(null);
			Product product = productRepository.findById(transfer.getProductId()).orElse(null);
			return mapToResponse(transfer, source, target, product);
		});
	}

	public Page<StockTransferResponse> searchTransfers(Long sourceWarehouseId, Long targetWarehouseId, Long productId,
			String status, String transferType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
		log.info("Searching stock transfers with filters");

		return transferRepository
				.searchTransfers(sourceWarehouseId, targetWarehouseId, productId, status, startDate, endDate, pageable)
				.map(transfer -> {
					Warehouse source = warehouseRepository.findById(transfer.getSourceWarehouseId()).orElse(null);
					Warehouse target = warehouseRepository.findById(transfer.getTargetWarehouseId()).orElse(null);
					Product product = productRepository.findById(transfer.getProductId()).orElse(null);
					return mapToResponse(transfer, source, target, product);
				});
	}

	public Page<StockTransferResponse> getTransfersBySourceWarehouse(Long warehouseId, Pageable pageable) {
		log.info("Fetching transfers by source warehouse: {}", warehouseId);
		return transferRepository.findBySourceWarehouseId(warehouseId, pageable).map(transfer -> {
			Warehouse source = warehouseRepository.findById(transfer.getSourceWarehouseId()).orElse(null);
			Warehouse target = warehouseRepository.findById(transfer.getTargetWarehouseId()).orElse(null);
			Product product = productRepository.findById(transfer.getProductId()).orElse(null);
			return mapToResponse(transfer, source, target, product);
		});
	}

	public Page<StockTransferResponse> getTransfersByTargetWarehouse(Long warehouseId, Pageable pageable) {
		log.info("Fetching transfers by target warehouse: {}", warehouseId);
		return transferRepository.findByTargetWarehouseId(warehouseId, pageable).map(transfer -> {
			Warehouse source = warehouseRepository.findById(transfer.getSourceWarehouseId()).orElse(null);
			Warehouse target = warehouseRepository.findById(transfer.getTargetWarehouseId()).orElse(null);
			Product product = productRepository.findById(transfer.getProductId()).orElse(null);
			return mapToResponse(transfer, source, target, product);
		});
	}

	public Page<StockTransferResponse> getTransfersByProduct(Long productId, Pageable pageable) {
		log.info("Fetching transfers by product: {}", productId);
		return transferRepository.findByProductId(productId, pageable).map(transfer -> {
			Warehouse source = warehouseRepository.findById(transfer.getSourceWarehouseId()).orElse(null);
			Warehouse target = warehouseRepository.findById(transfer.getTargetWarehouseId()).orElse(null);
			Product product = productRepository.findById(transfer.getProductId()).orElse(null);
			return mapToResponse(transfer, source, target, product);
		});
	}

	@Transactional
	public void cancelTransfer(Long transferId) {
		log.info("Cancelling stock transfer with id: {}", transferId);

		StockTransfer transfer = transferRepository.findById(transferId)
				.orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + transferId));

		if (!"PENDING".equals(transfer.getStatus())) {
			throw new BusinessException(
					"Only pending transfers can be cancelled. Current status: " + transfer.getStatus());
		}

		transfer.setStatus("CANCELLED");
		transferRepository.save(transfer);

		log.info("Stock transfer cancelled successfully. Transfer ID: {}", transferId);
	}

	public TransferStatistics getTransferStatistics() {
		log.info("Fetching transfer statistics");

		long totalTransfers = transferRepository.count();
		long completedTransfers = transferRepository.countByStatus("COMPLETED");
		long pendingTransfers = transferRepository.countByStatus("PENDING");
		long failedTransfers = transferRepository.countByStatus("FAILED");
		long cancelledTransfers = transferRepository.countByStatus("CANCELLED");

		return new TransferStatistics(totalTransfers, completedTransfers, pendingTransfers, failedTransfers,
				cancelledTransfers);
	}

	public TransferSummary getTransferSummary() {
		log.info("Fetching transfer summary");

		List<Object[]> summaryByStatus = transferRepository.getTransferSummary();

		TransferSummary summary = new TransferSummary();
		for (Object[] row : summaryByStatus) {
			String status = (String) row[0];
			Long count = (Long) row[1];
			Long totalQuantity = (Long) row[2];

			summary.addStatusSummary(status, count, totalQuantity);
		}

		return summary;
	}

	private StockTransferResponse mapToResponse(StockTransfer transfer, Warehouse source, Warehouse target,
			Product product) {
		StockTransferResponse response = new StockTransferResponse();
		response.setTransferId(transfer.getId());
		response.setTransferNumber(transfer.getTransferNumber());
		response.setSourceWarehouseId(source != null ? source.getId() : null);
		response.setSourceWarehouseName(source != null ? source.getName() : null);
		response.setSourceWarehouseLocation(source != null ? source.getLocation() : null);
		response.setTargetWarehouseId(target != null ? target.getId() : null);
		response.setTargetWarehouseName(target != null ? target.getName() : null);
		response.setTargetWarehouseLocation(target != null ? target.getLocation() : null);
		response.setProductId(product != null ? product.getId() : null);
		response.setProductName(product != null ? product.getName() : null);
		response.setProductSku(product != null ? product.getSku() : null);
		response.setQuantity(transfer.getQuantity());
		response.setTransferDate(transfer.getTransferDate());
		response.setStatus(transfer.getStatus());
		response.setTransferType(transfer.getTransferType());
		return response;
	}
}
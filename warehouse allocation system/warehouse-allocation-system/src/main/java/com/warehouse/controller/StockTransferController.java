package com.warehouse.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.payload.request.StockTransferRequest;
import com.warehouse.payload.response.StockTransferResponse;
import com.warehouse.payload.response.TransferStatistics;
import com.warehouse.payload.response.TransferSummary;
import com.warehouse.service.StockTransferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/stock-transfers")
@Slf4j
@Tag(name = "Stock Transfer Management", description = "APIs for managing stock transfers between warehouses")
public class StockTransferController {

	private static final Logger log = LoggerFactory.getLogger(StockTransferController.class);

	private final StockTransferService stockTransferService;

	public StockTransferController(StockTransferService stockTransferService) {
		this.stockTransferService = stockTransferService;
	}

	@PostMapping
	@Operation(summary = "Transfer stock between warehouses", description = "Transfer stock from source warehouse to target warehouse")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Stock transfer completed successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock"),
			@ApiResponse(responseCode = "404", description = "Warehouse or product not found"),
			@ApiResponse(responseCode = "409", description = "Concurrent modification conflict") })
	public ResponseEntity<StockTransferResponse> transferStock(@Valid @RequestBody StockTransferRequest request) {
		log.info("REST request to transfer stock from warehouse {} to {}", request.getSourceWarehouseId(),
				request.getTargetWarehouseId());
		StockTransferResponse response = stockTransferService.transferStock(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{transferId}")
	@Operation(summary = "Get stock transfer by ID", description = "Retrieve a specific stock transfer by its ID")
	public ResponseEntity<StockTransferResponse> getTransferById(
			@Parameter(description = "Transfer ID", required = true) @PathVariable Long transferId) {
		log.info("REST request to get stock transfer with id: {}", transferId);
		StockTransferResponse response = stockTransferService.getTransferById(transferId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@Operation(summary = "Get all stock transfers", description = "Retrieve all stock transfers with pagination and sorting")
	public ResponseEntity<Page<StockTransferResponse>> getAllTransfers(
			@Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Sort field") @RequestParam(defaultValue = "transferDate") String sortBy,
			@Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));

		Page<StockTransferResponse> transfers = stockTransferService.getAllTransfers(pageable);
		return ResponseEntity.ok(transfers);
	}

	@GetMapping("/search")
	@Operation(summary = "Search stock transfers", description = "Search stock transfers with multiple filters")
	public ResponseEntity<Page<StockTransferResponse>> searchTransfers(
			@Parameter(description = "Source warehouse ID") @RequestParam(required = false) Long sourceWarehouseId,
			@Parameter(description = "Target warehouse ID") @RequestParam(required = false) Long targetWarehouseId,
			@Parameter(description = "Product ID") @RequestParam(required = false) Long productId,
			@Parameter(description = "Transfer status (PENDING, COMPLETED, FAILED, CANCELLED)") @RequestParam(required = false) String status,
			@Parameter(description = "Transfer type (MANUAL, AUTO_REBALANCE)") @RequestParam(required = false) String transferType,
			@Parameter(description = "Start date (ISO format)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@Parameter(description = "End date (ISO format)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			@Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Sort field") @RequestParam(defaultValue = "transferDate") String sortBy,
			@Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));

		Page<StockTransferResponse> transfers = stockTransferService.searchTransfers(sourceWarehouseId,
				targetWarehouseId, productId, status, transferType, startDate, endDate, pageable);
		return ResponseEntity.ok(transfers);
	}

	@GetMapping("/by-source/{warehouseId}")
	@Operation(summary = "Get transfers by source warehouse", description = "Retrieve all transfers originating from a specific warehouse")
	public ResponseEntity<Page<StockTransferResponse>> getTransfersBySourceWarehouse(
			@Parameter(description = "Warehouse ID", required = true) @PathVariable Long warehouseId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("transferDate").descending());
		Page<StockTransferResponse> transfers = stockTransferService.getTransfersBySourceWarehouse(warehouseId,
				pageable);
		return ResponseEntity.ok(transfers);
	}

	@GetMapping("/by-target/{warehouseId}")
	@Operation(summary = "Get transfers by target warehouse", description = "Retrieve all transfers destined for a specific warehouse")
	public ResponseEntity<Page<StockTransferResponse>> getTransfersByTargetWarehouse(
			@Parameter(description = "Warehouse ID", required = true) @PathVariable Long warehouseId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("transferDate").descending());
		Page<StockTransferResponse> transfers = stockTransferService.getTransfersByTargetWarehouse(warehouseId,
				pageable);
		return ResponseEntity.ok(transfers);
	}

	@GetMapping("/by-product/{productId}")
	@Operation(summary = "Get transfers by product", description = "Retrieve all transfers for a specific product")
	public ResponseEntity<Page<StockTransferResponse>> getTransfersByProduct(
			@Parameter(description = "Product ID", required = true) @PathVariable Long productId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("transferDate").descending());
		Page<StockTransferResponse> transfers = stockTransferService.getTransfersByProduct(productId, pageable);
		return ResponseEntity.ok(transfers);
	}

	@DeleteMapping("/{transferId}/cancel")
	@Operation(summary = "Cancel stock transfer", description = "Cancel a pending stock transfer")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Transfer cancelled successfully"),
			@ApiResponse(responseCode = "400", description = "Transfer cannot be cancelled (not in pending state)"),
			@ApiResponse(responseCode = "404", description = "Transfer not found") })
	public ResponseEntity<Void> cancelTransfer(
			@Parameter(description = "Transfer ID", required = true) @PathVariable Long transferId) {
		log.info("REST request to cancel stock transfer with id: {}", transferId);
		stockTransferService.cancelTransfer(transferId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/statistics")
	@Operation(summary = "Get transfer statistics", description = "Get statistics about stock transfers")
	public ResponseEntity<TransferStatistics> getTransferStatistics() {
		log.info("REST request to get transfer statistics");
		TransferStatistics statistics = stockTransferService.getTransferStatistics();
		return ResponseEntity.ok(statistics);
	}

	@GetMapping("/summary")
	@Operation(summary = "Get transfer summary", description = "Get summary of transfers by status")
	public ResponseEntity<TransferSummary> getTransferSummary() {
		log.info("REST request to get transfer summary");
		TransferSummary summary = stockTransferService.getTransferSummary();
		return ResponseEntity.ok(summary);
	}
}
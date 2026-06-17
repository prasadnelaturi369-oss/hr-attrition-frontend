package com.warehouse.controller;

import java.time.LocalDateTime;
import java.util.List;

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

import com.warehouse.payload.request.AllocationRequest;
import com.warehouse.payload.response.AllocationResponse;
import com.warehouse.payload.response.AllocationStatistics;
import com.warehouse.service.AllocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/allocations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Allocation Management", description = "APIs for managing stock allocations")
public class AllocationController {

	private static final Logger log = LoggerFactory.getLogger(AllocationController.class);

	private final AllocationService allocationService;

	public AllocationController(AllocationService allocationService) {
		this.allocationService = allocationService;
	}

	@PostMapping
	@Operation(summary = "Allocate stock", description = "Allocate stock from optimal warehouse")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Stock allocated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock"),
			@ApiResponse(responseCode = "404", description = "Product or warehouse not found"),
			@ApiResponse(responseCode = "409", description = "Concurrent modification conflict") })
	public ResponseEntity<AllocationResponse> allocateStock(@Valid @RequestBody AllocationRequest request) {
		log.info("REST request to allocate stock for product: {}, quantity: {}", request.getProductId(),
				request.getQuantity());
		AllocationResponse response = allocationService.allocateStock(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{allocationId}")
	@Operation(summary = "Get allocation by ID", description = "Retrieve a specific allocation by its ID")
	public ResponseEntity<AllocationResponse> getAllocationById(
			@Parameter(description = "Allocation ID", required = true) @PathVariable Long allocationId) {
		log.info("REST request to get allocation with id: {}", allocationId);
		AllocationResponse response = allocationService.getAllocationById(allocationId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@Operation(summary = "Get all allocations", description = "Retrieve all allocations with pagination and sorting")
	public ResponseEntity<Page<AllocationResponse>> getAllAllocations(
			@Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Sort field") @RequestParam(defaultValue = "allocatedAt") String sortBy,
			@Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));

		Page<AllocationResponse> allocations = allocationService.getAllAllocations(pageable);
		return ResponseEntity.ok(allocations);
	}

	@DeleteMapping("/{allocationId}/cancel")
	@Operation(summary = "Cancel allocation", description = "Cancel an existing allocation")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Allocation cancelled successfully"),
			@ApiResponse(responseCode = "400", description = "Allocation cannot be cancelled"),
			@ApiResponse(responseCode = "404", description = "Allocation not found") })
	public ResponseEntity<Void> cancelAllocation(
			@Parameter(description = "Allocation ID", required = true) @PathVariable Long allocationId) {
		log.info("REST request to cancel allocation with id: {}", allocationId);
		allocationService.cancelAllocation(allocationId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	@Operation(summary = "Search allocations", description = "Search allocations with multiple filters")
	public ResponseEntity<Page<AllocationResponse>> searchAllocations(
			@Parameter(description = "Product ID") @RequestParam(required = false) Long productId,
			@Parameter(description = "Warehouse ID") @RequestParam(required = false) Long warehouseId,
			@Parameter(description = "Allocation status") @RequestParam(required = false) String status,
			@Parameter(description = "Start date (ISO format)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@Parameter(description = "End date (ISO format)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			@Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Sort field") @RequestParam(defaultValue = "allocatedAt") String sortBy,
			@Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));

		Page<AllocationResponse> allocations = allocationService.searchAllocations(productId, warehouseId, status,
				startDate, endDate, pageable);
		return ResponseEntity.ok(allocations);
	}

	@GetMapping("/by-product/{productId}")
	@Operation(summary = "Get allocations by product", description = "Retrieve all allocations for a specific product")
	public ResponseEntity<Page<AllocationResponse>> getAllocationsByProduct(
			@Parameter(description = "Product ID", required = true) @PathVariable Long productId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("allocatedAt").descending());
		Page<AllocationResponse> allocations = allocationService.getAllocationsByProduct(productId, pageable);
		return ResponseEntity.ok(allocations);
	}

	@GetMapping("/by-warehouse/{warehouseId}")
	@Operation(summary = "Get allocations by warehouse", description = "Retrieve all allocations for a specific warehouse")
	public ResponseEntity<Page<AllocationResponse>> getAllocationsByWarehouse(
			@Parameter(description = "Warehouse ID", required = true) @PathVariable Long warehouseId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("allocatedAt").descending());
		Page<AllocationResponse> allocations = allocationService.getAllocationsByWarehouse(warehouseId, pageable);
		return ResponseEntity.ok(allocations);
	}

	@GetMapping("/statistics")
	@Operation(summary = "Get allocation statistics", description = "Get statistics about allocations")
	public ResponseEntity<AllocationStatistics> getAllocationStatistics() {
		log.info("REST request to get allocation statistics");
		AllocationStatistics statistics = allocationService.getAllocationStatistics();
		return ResponseEntity.ok(statistics);
	}

	@GetMapping("/summary/by-product")
	@Operation(summary = "Get allocation summary by product", description = "Get allocation summary grouped by product")
	public ResponseEntity<List<Object[]>> getAllocationSummaryByProduct() {
		log.info("REST request to get allocation summary by product");
		List<Object[]> summary = allocationService.getAllocationSummaryByProduct();
		return ResponseEntity.ok(summary);
	}

	@GetMapping("/summary/by-warehouse")
	@Operation(summary = "Get allocation summary by warehouse", description = "Get allocation summary grouped by warehouse")
	public ResponseEntity<List<Object[]>> getAllocationSummaryByWarehouse() {
		log.info("REST request to get allocation summary by warehouse");
		List<Object[]> summary = allocationService.getAllocationSummaryByWarehouse();
		return ResponseEntity.ok(summary);
	}
}
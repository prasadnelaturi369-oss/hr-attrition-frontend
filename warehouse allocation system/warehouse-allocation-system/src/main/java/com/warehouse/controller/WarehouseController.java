package com.warehouse.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.payload.request.WarehouseRequest;
import com.warehouse.payload.response.WarehouseResponse;
import com.warehouse.service.WarehouseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Warehouse Management", description = "APIs for managing warehouses")
public class WarehouseController {

	private static final Logger log = LoggerFactory.getLogger(StockTransferController.class);

	private final WarehouseService warehouseService;

	public WarehouseController(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@PostMapping
	@Operation(summary = "Create warehouse", description = "Create a new warehouse")
	public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
		log.info("REST request to create warehouse: {}", request.getName());
		WarehouseResponse response = warehouseService.createWarehouse(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get warehouse by ID", description = "Retrieve a specific warehouse by its ID")
	public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
		log.info("REST request to get warehouse with id: {}", id);
		WarehouseResponse response = warehouseService.getWarehouseById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@Operation(summary = "Get all warehouses", description = "Retrieve all warehouses with pagination")
	public ResponseEntity<Page<WarehouseResponse>> getAllWarehouses(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));

		Page<WarehouseResponse> warehouses = warehouseService.getAllWarehouses(pageable);
		return ResponseEntity.ok(warehouses);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update warehouse", description = "Update an existing warehouse")
	public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id,
			@Valid @RequestBody WarehouseRequest request) {
		log.info("REST request to update warehouse with id: {}", id);
		WarehouseResponse response = warehouseService.updateWarehouse(id, request);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/activate")
	@Operation(summary = "Activate warehouse", description = "Activate a warehouse")
	public ResponseEntity<WarehouseResponse> activateWarehouse(@PathVariable Long id) {
		log.info("REST request to activate warehouse with id: {}", id);
		WarehouseResponse response = warehouseService.activateWarehouse(id);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/deactivate")
	@Operation(summary = "Deactivate warehouse", description = "Deactivate a warehouse")
	public ResponseEntity<WarehouseResponse> deactivateWarehouse(@PathVariable Long id) {
		log.info("REST request to deactivate warehouse with id: {}", id);
		WarehouseResponse response = warehouseService.deactivateWarehouse(id);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Soft delete warehouse", description = "Soft delete a warehouse")
	public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
		log.info("REST request to soft delete warehouse with id: {}", id);
		warehouseService.softDeleteWarehouse(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/active")
	@Operation(summary = "Get active warehouses", description = "Retrieve all active warehouses")
	public ResponseEntity<List<WarehouseResponse>> getActiveWarehouses() {
		log.info("REST request to get all active warehouses");
		List<WarehouseResponse> warehouses = warehouseService.getActiveWarehouses();
		return ResponseEntity.ok(warehouses);
	}
}
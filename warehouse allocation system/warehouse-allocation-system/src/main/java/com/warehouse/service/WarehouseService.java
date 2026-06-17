package com.warehouse.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.entity.Warehouse;
import com.warehouse.exception.BusinessException;
import com.warehouse.exception.ResourceNotFoundException;
import com.warehouse.payload.request.WarehouseRequest;
import com.warehouse.payload.response.WarehouseResponse;
import com.warehouse.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
	private static final Logger log = LoggerFactory.getLogger(WarehouseService.class);

	private final WarehouseRepository warehouseRepository;

	public WarehouseService(WarehouseRepository warehouseRepository) {
		this.warehouseRepository = warehouseRepository;
	}

	@Transactional
	public WarehouseResponse createWarehouse(WarehouseRequest request) {
		log.info("Creating new warehouse: {}", request.getName());

		if (warehouseRepository.findByNameAndDeletedAtIsNull(request.getName()).isPresent()) {
			throw new BusinessException("Warehouse with name '" + request.getName() + "' already exists");
		}

		Optional<Warehouse> deletedWarehouse = warehouseRepository.findByName(request.getName())
				.filter(w -> w.getDeletedAt() != null);

		if (deletedWarehouse.isPresent()) {
			log.info("Found soft-deleted warehouse '{}', reactivating it", request.getName());
			Warehouse existingWarehouse = deletedWarehouse.get();
			existingWarehouse.setDeletedAt(null);
			existingWarehouse.setStatus("ACTIVE");
			existingWarehouse.setLocation(request.getLocation());
			existingWarehouse.setCapacity(request.getCapacity());
			Warehouse reactivatedWarehouse = warehouseRepository.save(existingWarehouse);
			return mapToResponse(reactivatedWarehouse);
		}

		Warehouse warehouse = new Warehouse();
		warehouse.setName(request.getName());
		warehouse.setLocation(request.getLocation());
		warehouse.setCapacity(request.getCapacity());
		warehouse.setStatus("ACTIVE");

		Warehouse savedWarehouse = warehouseRepository.save(warehouse);
		return mapToResponse(savedWarehouse);
	}

	public WarehouseResponse getWarehouseById(Long id) {
		log.info("Fetching warehouse with id: {}", id);
		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
		return mapToResponse(warehouse);
	}

	public Page<WarehouseResponse> getAllWarehouses(Pageable pageable) {
		log.info("Fetching all warehouses with pagination");
		return warehouseRepository.findAllActive(pageable).map(this::mapToResponse);
	}

	@Transactional
	public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
		log.info("Updating warehouse with id: {}", id);

		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

		if (request.getName() != null && !request.getName().equals(warehouse.getName())) {
			if (warehouseRepository.findByName(request.getName()).isPresent()) {
				throw new BusinessException("Warehouse with name '" + request.getName() + "' already exists");
			}
			warehouse.setName(request.getName());
		}

		if (request.getLocation() != null) {
			warehouse.setLocation(request.getLocation());
		}

		if (request.getCapacity() != null) {
			warehouse.setCapacity(request.getCapacity());
		}

		Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
		return mapToResponse(updatedWarehouse);
	}

	@Transactional
	public WarehouseResponse activateWarehouse(Long id) {
		log.info("Activating warehouse with id: {}", id);

		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

		warehouse.setStatus("ACTIVE");
		warehouse.setDeletedAt(null);

		Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
		return mapToResponse(updatedWarehouse);
	}

	@Transactional
	public WarehouseResponse deactivateWarehouse(Long id) {
		log.info("Deactivating warehouse with id: {}", id);

		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

		warehouse.setStatus("INACTIVE");

		Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
		return mapToResponse(updatedWarehouse);
	}

	@Transactional
	public void softDeleteWarehouse(Long id) {
		log.info("Soft deleting warehouse with id: {}", id);

		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

		warehouse.setStatus("DELETED");
		warehouse.setDeletedAt(LocalDateTime.now());
		warehouseRepository.save(warehouse);
	}

	public List<WarehouseResponse> getActiveWarehouses() {
		log.info("Fetching all active warehouses");
		return warehouseRepository.findByStatusAndDeletedAtIsNull("ACTIVE").stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private WarehouseResponse mapToResponse(Warehouse warehouse) {
		WarehouseResponse response = new WarehouseResponse();
		response.setId(warehouse.getId());
		response.setName(warehouse.getName());
		response.setLocation(warehouse.getLocation());
		response.setCapacity(warehouse.getCapacity());
		response.setStatus(warehouse.getStatus());
		response.setCreatedAt(warehouse.getCreatedAt());
		response.setUpdatedAt(warehouse.getUpdatedAt());
		return response;
	}
}
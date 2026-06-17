package com.warehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

	Page<Warehouse> findByStatusAndDeletedAtIsNull(String status, Pageable pageable);

	List<Warehouse> findByStatusAndDeletedAtIsNull(String status);

	@Query("SELECT w FROM Warehouse w WHERE w.status = 'ACTIVE' AND w.deletedAt IS NULL AND w.capacity > "
			+ "(SELECT COALESCE(SUM(wi.availableQuantity), 0) FROM WarehouseInventory wi WHERE wi.warehouse = w)")
	List<Warehouse> findWarehousesWithAvailableCapacity();

	@Query("SELECT w FROM Warehouse w WHERE w.deletedAt IS NULL")
	Page<Warehouse> findAllActive(Pageable pageable);

	@Query("SELECT w FROM Warehouse w WHERE w.name = :name AND w.deletedAt IS NULL")
	Optional<Warehouse> findByNameAndDeletedAtIsNull(String name);

	Optional<Warehouse> findByName(String name);

	@Query("SELECT COUNT(w) > 0 FROM Warehouse w WHERE w.name = :name AND w.deletedAt IS NULL")
	boolean existsByNameAndDeletedAtIsNull(String name);
}
package com.warehouse.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.Allocation;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

	Page<Allocation> findByProductId(Long productId, Pageable pageable);

	Page<Allocation> findByWarehouseId(Long warehouseId, Pageable pageable);

	Page<Allocation> findByStatus(String status, Pageable pageable);

	Page<Allocation> findByAllocatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	List<Allocation> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

	@Query("SELECT a FROM Allocation a WHERE a.product.id = :productId AND a.status IN ('ALLOCATED')")
	List<Allocation> findActiveAllocationsByProductId(@Param("productId") Long productId);

	long countByStatus(String status);

	@Query("SELECT COALESCE(SUM(a.quantity), 0) FROM Allocation a WHERE a.product.id = :productId AND a.status = 'ALLOCATED'")
	Integer getTotalAllocatedQuantityByProductId(@Param("productId") Long productId);

	@Query("SELECT COALESCE(SUM(a.quantity), 0) FROM Allocation a WHERE a.warehouse.id = :warehouseId AND a.status = 'ALLOCATED'")
	Integer getTotalAllocatedQuantityByWarehouseId(@Param("warehouseId") Long warehouseId);

	@Query("SELECT a FROM Allocation a WHERE a.allocatedAt >= :since")
	List<Allocation> findAllocationsSince(@Param("since") LocalDateTime since);

	@Query("SELECT a FROM Allocation a WHERE " + "(:productId IS NULL OR a.product.id = :productId) AND "
			+ "(:warehouseId IS NULL OR a.warehouse.id = :warehouseId) AND "
			+ "(:status IS NULL OR a.status = :status) AND "
			+ "(:startDate IS NULL OR a.allocatedAt >= :startDate) AND "
			+ "(:endDate IS NULL OR a.allocatedAt <= :endDate)")
	Page<Allocation> searchAllocations(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId,
			@Param("status") String status, @Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate, Pageable pageable);

	@Query("SELECT a FROM Allocation a WHERE "
			+ "(:allocationNumber IS NULL OR a.allocationNumber LIKE CONCAT('%', :allocationNumber, '%')) AND "
			+ "(:productId IS NULL OR a.product.id = :productId) AND "
			+ "(:warehouseId IS NULL OR a.warehouse.id = :warehouseId)")
	Page<Allocation> advancedSearch(@Param("allocationNumber") String allocationNumber,
			@Param("productId") Long productId, @Param("warehouseId") Long warehouseId, Pageable pageable);

	@Query("SELECT a.product.id, a.product.name, COUNT(a), SUM(a.quantity) "
			+ "FROM Allocation a GROUP BY a.product.id, a.product.name")
	List<Object[]> getAllocationSummaryByProduct();

	@Query("SELECT a.warehouse.id, a.warehouse.name, COUNT(a), SUM(a.quantity) "
			+ "FROM Allocation a GROUP BY a.warehouse.id, a.warehouse.name")
	List<Object[]> getAllocationSummaryByWarehouse();

	List<Allocation> findByReferenceNumber(String referenceNumber);

	Page<Allocation> findByProductIdAndAllocatedAtBetween(Long productId, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable);

	Page<Allocation> findByWarehouseIdAndAllocatedAtBetween(Long warehouseId, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable);

	@Query("SELECT a FROM Allocation a ORDER BY a.allocatedAt DESC")
	Page<Allocation> findRecentAllocations(Pageable pageable);

	boolean existsByAllocationNumber(String allocationNumber);
}
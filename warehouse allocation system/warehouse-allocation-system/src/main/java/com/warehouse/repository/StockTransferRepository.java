package com.warehouse.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.StockTransfer;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

	Page<StockTransfer> findBySourceWarehouseId(Long sourceWarehouseId, Pageable pageable);

	Page<StockTransfer> findByTargetWarehouseId(Long targetWarehouseId, Pageable pageable);

	Page<StockTransfer> findByProductId(Long productId, Pageable pageable);

	Page<StockTransfer> findByStatus(String status, Pageable pageable);

	Page<StockTransfer> findByTransferDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	List<StockTransfer> findBySourceWarehouseIdAndTargetWarehouseId(Long sourceId, Long targetId);

	List<StockTransfer> findByStatus(String status);

	@Query("SELECT st FROM StockTransfer st WHERE "
			+ "(:sourceWarehouseId IS NULL OR st.sourceWarehouseId = :sourceWarehouseId) AND "
			+ "(:targetWarehouseId IS NULL OR st.targetWarehouseId = :targetWarehouseId) AND "
			+ "(:productId IS NULL OR st.productId = :productId) AND " + "(:status IS NULL OR st.status = :status) AND "
			+ "(:startDate IS NULL OR st.transferDate >= :startDate) AND "
			+ "(:endDate IS NULL OR st.transferDate <= :endDate)")
	Page<StockTransfer> searchTransfers(@Param("sourceWarehouseId") Long sourceWarehouseId,
			@Param("targetWarehouseId") Long targetWarehouseId, @Param("productId") Long productId,
			@Param("status") String status, @Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate, Pageable pageable);

	@Query("SELECT st.status, COUNT(st), SUM(st.quantity) FROM StockTransfer st GROUP BY st.status")
	List<Object[]> getTransferSummary();

	StockTransfer findByTransferNumber(String transferNumber);

	boolean existsByTransferNumber(String transferNumber);

	long countByStatus(String string);
}
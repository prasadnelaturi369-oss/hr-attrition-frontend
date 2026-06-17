package com.warehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;

import jakarta.persistence.LockModeType;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Long> {

	Optional<WarehouseInventory> findByWarehouseAndProduct(Warehouse warehouse, Product product);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT wi FROM WarehouseInventory wi WHERE wi.warehouse = :warehouse AND wi.product = :product")
	Optional<WarehouseInventory> findByWarehouseAndProductWithLock(@Param("warehouse") Warehouse warehouse,
			@Param("product") Product product);

	Page<WarehouseInventory> findByWarehouse(Warehouse warehouse, Pageable pageable);

	List<WarehouseInventory> findByProduct(Product product);

	@Query("SELECT wi FROM WarehouseInventory wi " + "WHERE wi.product = :product " + "AND wi.availableQuantity > 0 "
			+ "ORDER BY wi.availableQuantity DESC")
	List<WarehouseInventory> findAvailableWarehousesForProduct(@Param("product") Product product);

	@Query("SELECT wi FROM WarehouseInventory wi " + "WHERE wi.product = :product "
			+ "AND wi.availableQuantity >= :quantity")
	List<WarehouseInventory> findWarehousesWithSufficientStock(@Param("product") Product product,
			@Param("quantity") Integer quantity);

	@Query("SELECT wi FROM WarehouseInventory wi " + "WHERE wi.warehouse = :warehouse "
			+ "AND wi.availableQuantity < :threshold")
	List<WarehouseInventory> findLowStockInventory(@Param("warehouse") Warehouse warehouse,
			@Param("threshold") Integer threshold);

	@Query("SELECT wi FROM WarehouseInventory wi " + "WHERE wi.availableQuantity < :threshold")
	List<WarehouseInventory> findAllLowStockInventory(@Param("threshold") Integer threshold);

	@Modifying
	@Transactional
	@Query("UPDATE WarehouseInventory wi SET " + "wi.availableQuantity = wi.availableQuantity - :quantity, "
			+ "wi.allocatedQuantity = wi.allocatedQuantity + :quantity " + "WHERE wi.id = :inventoryId "
			+ "AND wi.availableQuantity >= :quantity")
	int allocateStock(@Param("inventoryId") Long inventoryId, @Param("quantity") Integer quantity);

	@Modifying
	@Transactional
	@Query("UPDATE WarehouseInventory wi SET " + "wi.availableQuantity = wi.availableQuantity + :quantity, "
			+ "wi.allocatedQuantity = wi.allocatedQuantity - :quantity " + "WHERE wi.id = :inventoryId "
			+ "AND wi.allocatedQuantity >= :quantity")
	int cancelAllocation(@Param("inventoryId") Long inventoryId, @Param("quantity") Integer quantity);

	@Modifying
	@Transactional
	@Query("UPDATE WarehouseInventory wi SET " + "wi.availableQuantity = wi.availableQuantity + :quantity "
			+ "WHERE wi.id = :inventoryId")
	int addStock(@Param("inventoryId") Long inventoryId, @Param("quantity") Integer quantity);

	@Modifying
	@Transactional
	@Query("UPDATE WarehouseInventory wi SET " + "wi.availableQuantity = wi.availableQuantity - :quantity "
			+ "WHERE wi.id = :inventoryId " + "AND wi.availableQuantity >= :quantity")
	int removeStock(@Param("inventoryId") Long inventoryId, @Param("quantity") Integer quantity);

	@Query("SELECT wi.warehouse.id, wi.warehouse.name, " + "SUM(wi.availableQuantity * p.price) "
			+ "FROM WarehouseInventory wi " + "JOIN wi.product p " + "GROUP BY wi.warehouse.id, wi.warehouse.name")
	List<Object[]> getInventoryValueByWarehouse();

	@Modifying
	@Transactional
	@Query("UPDATE WarehouseInventory wi SET " + "wi.availableQuantity = :quantity " + "WHERE wi.id = :id")
	int updateAvailableQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

	boolean existsByWarehouseAndProduct(Warehouse warehouse, Product product);

	@Query("SELECT COALESCE(SUM(wi.availableQuantity), 0) " + "FROM WarehouseInventory wi "
			+ "WHERE wi.product = :product")
	Integer getTotalAvailableStockForProduct(@Param("product") Product product);
}
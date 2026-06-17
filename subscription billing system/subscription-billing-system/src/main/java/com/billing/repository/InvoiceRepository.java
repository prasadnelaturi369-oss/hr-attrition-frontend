package com.billing.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.billing.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	Page<Invoice> findByUserId(Long userId, Pageable pageable);

	Page<Invoice> findByStatus(String status, Pageable pageable);

	long countByStatus(String status);

	long countByUserId(Long userId);

	long countByUserIdAndStatus(Long userId, String status);

	@Query("SELECT i FROM Invoice i WHERE i.dueDate < :now AND i.status = 'PENDING'")
	Page<Invoice> findOverdueInvoices(@Param("now") LocalDateTime now, Pageable pageable);
}
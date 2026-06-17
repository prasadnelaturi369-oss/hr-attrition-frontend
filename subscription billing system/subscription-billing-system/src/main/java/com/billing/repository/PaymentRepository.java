package com.billing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.billing.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Page<Payment> findByUserId(Long userId, Pageable pageable);

	Page<Payment> findByStatus(String status, Pageable pageable);

	long countByUserId(Long userId);

	@Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
	Double getTotalRevenue();

	@Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE DATE_FORMAT(p.paymentDate, '%Y-%m') = :month")
	Double getMonthlyRevenue(@Param("month") String month);

	@Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.user.id = :userId")
	Double getTotalSpentByUser(@Param("userId") Long userId);
}
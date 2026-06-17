package com.billing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.billing.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	Page<Subscription> findByUserId(Long userId, Pageable pageable);

	Page<Subscription> findByStatus(String status, Pageable pageable);

	long countByStatus(String status);

	boolean existsByUserIdAndStatus(Long userId, String status);

	long countByUserIdAndStatus(Long userId, String status);

	@Query("SELECT s FROM Subscription s WHERE s.endDate < :now AND s.status = 'ACTIVE'")
	List<Subscription> findExpiredSubscriptions(@Param("now") LocalDateTime now);

	@Query("SELECT s FROM Subscription s WHERE s.user.id = :userId ORDER BY s.createdAt DESC")
	Page<Subscription> findUserSubscriptions(@Param("userId") Long userId, Pageable pageable);
}
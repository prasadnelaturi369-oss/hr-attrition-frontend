package com.banking.transaction.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.transaction.entity.Transaction;
import com.banking.transaction.entity.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

	Optional<Transaction> findByTransactionRef(String transactionRef);

	List<Transaction> findByFromAccountIdOrderByCreatedAtDesc(UUID fromAccountId);

	List<Transaction> findByToAccountIdOrderByCreatedAtDesc(UUID toAccountId);

	List<Transaction> findByStatus(TransactionStatus status);

	List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	long countByFromAccountIdAndCreatedAtBetween(UUID fromAccountId, LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.fromAccountId = :accountId AND t.status = 'COMPLETED' AND t.createdAt BETWEEN :startDate AND :endDate")
	BigDecimal sumAmountByFromAccountAndDateRange(@Param("accountId") UUID accountId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("SELECT t FROM Transaction t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdAt DESC")
	List<Transaction> findAllByAccountId(@Param("accountId") UUID accountId);

	@Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = :status AND t.createdAt < :date")
	long countByStatusAndCreatedAtBefore(@Param("status") TransactionStatus status, @Param("date") LocalDateTime date);

	Page<Transaction> findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(UUID fromAccountId, UUID toAccountId,
			Pageable pageable);
}
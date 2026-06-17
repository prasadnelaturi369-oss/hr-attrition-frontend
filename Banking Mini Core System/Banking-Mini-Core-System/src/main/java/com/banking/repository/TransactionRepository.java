package com.banking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.entity.Account;
import com.banking.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> { 

	List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);

	Optional<Transaction> findByTransactionReference(String reference);

	@Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate ASC")
	List<Transaction> findTransactionsBetweenDates(@Param("accountId") Long accountId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
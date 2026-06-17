package com.banking.account.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.account.entity.Account;
import com.banking.account.entity.AccountStatus;
import com.banking.account.entity.AccountType;

import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, UUID> {

	List<Account> findByUserId(UUID userId);

	Optional<Account> findByAccountNumber(String accountNumber);

	List<Account> findByUserIdAndStatus(UUID userId, AccountStatus status);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.id = :id")
	Optional<Account> findByIdWithLock(@Param("id") UUID id);

	long countByUserId(UUID userId);

	boolean existsByUserIdAndAccountType(UUID userId, AccountType accountType);
}
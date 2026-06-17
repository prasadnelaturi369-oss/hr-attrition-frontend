package com.banking.transaction.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.banking.transaction.entity.Transaction;

@Repository
public class TransactionRepository {
	private final Map<String, Transaction> transactionStore = new ConcurrentHashMap<>();

	public Transaction save(Transaction transaction) {
		transactionStore.put(transaction.getId(), transaction);
		return transaction;
	}

	public Optional<Transaction> findById(String id) {
		return Optional.ofNullable(transactionStore.get(id));
	}

	public List<Transaction> findByAccount(String accountId) {
		return transactionStore.values().stream()
				.filter(t -> t.getFromAccountId().equals(accountId) || t.getToAccountId().equals(accountId))
				.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).collect(Collectors.toList());
	}
}
package com.banking.account.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.banking.account.entity.Account;

@Repository
public class AccountRepository {

	private final Map<String, Account> accountStore = new ConcurrentHashMap<>();

	public Account save(Account account) {
		accountStore.put(account.getId(), account);
		return account;
	}

	public Optional<Account> findById(String id) {
		return Optional.ofNullable(accountStore.get(id));
	}

	public List<Account> findByCustomerId(String customerId) {
		return accountStore.values().stream().filter(account -> account.getCustomerId().equals(customerId))
				.collect(Collectors.toList());
	}
}
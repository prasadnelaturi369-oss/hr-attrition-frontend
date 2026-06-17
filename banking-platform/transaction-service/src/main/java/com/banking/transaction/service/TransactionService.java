package com.banking.transaction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.transaction.entity.Transaction;
import com.banking.transaction.payload.request.TransferRequest;
import com.banking.transaction.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public Transaction transferFunds(TransferRequest request) {
		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setToAccountId(request.getToAccountId());
		transaction.setAmount(request.getAmount());
		transaction.setCurrency("USD");
		transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Fund Transfer");

		return transactionRepository.save(transaction);
	}

	public List<Transaction> getAccountTransactions(String accountId) {
		return transactionRepository.findByAccount(accountId);
	}
}
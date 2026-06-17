package com.banking.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.transaction.entity.Transaction;
import com.banking.transaction.payload.request.TransferRequest;
import com.banking.transaction.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/transfer")
	public Transaction transfer(@RequestBody TransferRequest request) {
		return transactionService.transferFunds(request);
	}

	@GetMapping("/account/{accountId}")
	public List<Transaction> getAccountTransactions(@PathVariable String accountId) {
		return transactionService.getAccountTransactions(accountId);
	}
}
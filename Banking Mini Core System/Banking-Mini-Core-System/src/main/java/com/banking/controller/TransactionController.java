package com.banking.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.payload.request.DepositRequest;
import com.banking.payload.request.TransferRequest;
import com.banking.payload.request.WithdrawRequest;
import com.banking.payload.response.StatementResponse;
import com.banking.payload.response.TransactionResponse;
import com.banking.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "APIs for banking transactions")
public class TransactionController {

	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/deposit")
	@Operation(summary = "Deposit money to account")
	public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
		log.info("POST /api/transactions/deposit - Account: {}, Amount: {}", request.getAccountNumber(),
				request.getAmount());
		return ResponseEntity.ok(transactionService.deposit(request));
	}

	@PostMapping("/withdraw")
	@Operation(summary = "Withdraw money from account")
	public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
		log.info("POST /api/transactions/withdraw - Account: {}, Amount: {}", request.getAccountNumber(),
				request.getAmount());
		return ResponseEntity.ok(transactionService.withdraw(request));
	}

	@PostMapping("/transfer")
	@Operation(summary = "Transfer funds between accounts")
	public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
		log.info("POST /api/transactions/transfer - From: {}, To: {}, Amount: {}", request.getFromAccountNumber(),
				request.getToAccountNumber(), request.getAmount());
		return ResponseEntity.ok(transactionService.transferFunds(request));
	}

	@GetMapping("/statement")
	@Operation(summary = "Get account statement")
	public ResponseEntity<StatementResponse> getStatement(@RequestParam String accountNumber,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		log.info("GET /api/transactions/statement - Account: {}, From: {}, To: {}", accountNumber, fromDate, toDate);
		return ResponseEntity.ok(transactionService.getAccountStatement(accountNumber, fromDate, toDate));
	}
}
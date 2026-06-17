package com.banking.transaction.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.transaction.entity.TransactionStatus;
import com.banking.transaction.payload.request.TransferRequest;
import com.banking.transaction.payload.response.TransactionHistoryResponse;
import com.banking.transaction.payload.response.TransferResponse;
import com.banking.transaction.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/transfer")
	public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
		log.info("REST request to transfer funds");
		TransferResponse response = transactionService.transferFunds(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/reference/{transactionRef}")
	public ResponseEntity<TransactionHistoryResponse> getTransactionByReference(@PathVariable String transactionRef) {
		log.info("REST request to get transaction by reference: {}", transactionRef);
		TransactionHistoryResponse response = transactionService.getTransactionByReference(transactionRef);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/account/{accountId}")
	public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(@PathVariable UUID accountId) {
		log.info("REST request to get transaction history for account: {}", accountId);
		List<TransactionHistoryResponse> history = transactionService.getTransactionHistory(accountId);
		return ResponseEntity.ok(history);
	}

	@GetMapping("/account/{accountId}/paginated")
	public ResponseEntity<Page<TransactionHistoryResponse>> getTransactionHistoryPaginated(@PathVariable UUID accountId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		log.info("REST request to get paginated transaction history for account: {}", accountId);
		Page<TransactionHistoryResponse> history = transactionService.getTransactionHistoryPaginated(accountId, page,
				size);
		return ResponseEntity.ok(history);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<TransactionHistoryResponse>> getTransactionsByStatus(
			@PathVariable TransactionStatus status) {
		log.info("REST request to get transactions by status: {}", status);
		List<TransactionHistoryResponse> transactions = transactionService.getTransactionsByStatus(status);
		return ResponseEntity.ok(transactions);
	}

	@GetMapping("/account/{accountId}/daily-debit")
	public ResponseEntity<BigDecimal> getTotalDailyDebit(@PathVariable UUID accountId) {
		log.info("REST request to get total daily debit for account: {}", accountId);
		BigDecimal total = transactionService.getTotalDailyDebit(accountId);
		return ResponseEntity.ok(total);
	}

	@DeleteMapping("/cleanup")
	public ResponseEntity<Void> cleanupFailedTransactions() {
		log.info("REST request to cleanup failed transactions");
		transactionService.cleanupFailedTransactions();
		return ResponseEntity.ok().build();
	}
}
package com.banking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.enums.TransactionType;
import com.banking.exception.BankingException;
import com.banking.payload.request.DepositRequest;
import com.banking.payload.request.TransferRequest;
import com.banking.payload.request.WithdrawRequest;
import com.banking.payload.response.StatementResponse;
import com.banking.payload.response.TransactionResponse;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;

@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final AccountService accountService;
	private final AuditService auditService;
	private final ReentrantLock transferLock = new ReentrantLock();

	public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository,
			AccountService accountService, AuditService auditService) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.accountService = accountService;
		this.auditService = auditService;
	}

	// ===================== DEPOSIT =====================
	@Transactional
	public TransactionResponse deposit(DepositRequest request) {

		Account account = getActiveAccount(request.getAccountNumber());

		BigDecimal newBalance = account.getBalance().add(request.getAmount());
		account.setBalance(newBalance);
		accountRepository.save(account);

		Transaction tx = createTransaction(account, request.getAmount(), TransactionType.DEPOSIT, newBalance,
				request.getDescription());

		tx = transactionRepository.save(tx);

		auditService.log("DEPOSIT", "Transaction", String.valueOf(tx.getId()), "Deposited ₹" + request.getAmount());

		return buildTransactionResponse(tx, "Deposit successful");
	}

	// ===================== WITHDRAW =====================
	@Transactional
	public TransactionResponse withdraw(WithdrawRequest request) {

		Account account = getActiveAccount(request.getAccountNumber());

		accountService.validateSufficientBalance(request.getAccountNumber(), request.getAmount());

		BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
		account.setBalance(newBalance);
		accountRepository.save(account);

		Transaction tx = createTransaction(account, request.getAmount(), TransactionType.WITHDRAWAL, newBalance,
				request.getDescription());

		tx = transactionRepository.save(tx);

		auditService.log("WITHDRAWAL", "Transaction", String.valueOf(tx.getId()), "Withdrew ₹" + request.getAmount());

		return buildTransactionResponse(tx, "Withdrawal successful");
	}

	// ===================== TRANSFER =====================
	@Transactional
	public TransactionResponse transferFunds(TransferRequest request) {

		transferLock.lock();
		try {
			Account from = getActiveAccount(request.getFromAccountNumber());
			Account to = getActiveAccount(request.getToAccountNumber());

			if (from.getId().equals(to.getId())) {
				throw new BankingException("Cannot transfer to same account");
			}

			accountService.validateSufficientBalance(request.getFromAccountNumber(), request.getAmount());

			BigDecimal fromBalance = from.getBalance().subtract(request.getAmount());
			BigDecimal toBalance = to.getBalance().add(request.getAmount());

			from.setBalance(fromBalance);
			to.setBalance(toBalance);

			accountRepository.save(from);
			accountRepository.save(to);

			// Debit Transaction
			Transaction debitTx = createTransaction(from, request.getAmount(), TransactionType.TRANSFER, fromBalance,
					"Transfer to: " + to.getAccountNumber());
			debitTx.setRelatedAccountId(to.getId());

			// Credit Transaction (IMPORTANT)
			Transaction creditTx = createTransaction(to, request.getAmount(), TransactionType.DEPOSIT, toBalance,
					"Received from: " + from.getAccountNumber());
			creditTx.setRelatedAccountId(from.getId());

			transactionRepository.save(debitTx);
			transactionRepository.save(creditTx);

			auditService.log("TRANSFER", "Transaction", String.valueOf(debitTx.getId()),
					"Transferred ₹" + request.getAmount());

			return buildTransactionResponse(debitTx, "Transfer successful");

		} finally {
			transferLock.unlock();
		}
	}

	// ===================== STATEMENT =====================
	@Transactional(readOnly = true)
	public StatementResponse getAccountStatement(String accountNumber, LocalDate fromDate, LocalDate toDate) {

		Account account = getActiveAccount(accountNumber);

		LocalDateTime start = fromDate.atStartOfDay();
		LocalDateTime end = toDate.atTime(LocalTime.MAX);

		BigDecimal openingBalance = getBalanceBeforeDate(account.getId(), start);

		var transactions = transactionRepository.findTransactionsBetweenDates(account.getId(), start, end);

		var entries = transactions.stream().map(tx -> {
			StatementResponse.StatementEntry e = new StatementResponse.StatementEntry();

			e.setDate(tx.getTransactionDate());
			e.setType(tx.getTransactionType().name());
			e.setAmount(tx.getAmount());
			e.setDescription(tx.getDescription());
			e.setBalanceAfter(tx.getBalanceAfterTransaction());
			return e;
		}).collect(Collectors.toList());

		BigDecimal closingBalance = openingBalance;
		if (!transactions.isEmpty()) {
			closingBalance = transactions.get(transactions.size() - 1).getBalanceAfterTransaction();
		}

		StatementResponse response = new StatementResponse();
		response.setAccountNumber(account.getAccountNumber());
		response.setCustomerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
		response.setFromDate(start);
		response.setToDate(end);
		response.setOpeningBalance(openingBalance);
		response.setTransactions(entries);
		response.setClosingBalance(closingBalance);

		return StatementResponse.success(response, "Statement generated successfully");
	}

	// ===================== HELPERS =====================

	private Account getActiveAccount(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BankingException("Account not found"));

		if (!account.getIsActive()) {
			throw new BankingException("Account is inactive");
		}
		return account;
	}

	private Transaction createTransaction(Account account, BigDecimal amount, TransactionType type,
			BigDecimal balanceAfter, String description) {

		Transaction tx = new Transaction();
		tx.setAccount(account);
		tx.setAmount(amount);
		tx.setTransactionType(type);
		tx.setBalanceAfterTransaction(balanceAfter);
		tx.setDescription(description);
		return tx;
	}

	private BigDecimal getBalanceBeforeDate(Long accountId, LocalDateTime dateTime) {

		var transactions = transactionRepository.findTransactionsBetweenDates(accountId, LocalDateTime.MIN, dateTime);

		if (transactions.isEmpty())
			return BigDecimal.ZERO;

		return transactions.get(transactions.size() - 1).getBalanceAfterTransaction();
	}

	private TransactionResponse buildTransactionResponse(Transaction tx, String message) {

		TransactionResponse res = new TransactionResponse();
		res.setTransactionId(tx.getId());
		res.setTransactionReference(tx.getTransactionReference());
		res.setTransactionType(tx.getTransactionType().name());
		res.setStatus(tx.getStatus().name());
		res.setAmount(tx.getAmount());
		res.setBalanceAfter(tx.getBalanceAfterTransaction());
		res.setDescription(tx.getDescription());
		res.setTransactionDate(tx.getTransactionDate());

		return TransactionResponse.success(res, message);
	}
}
package com.banking.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.transaction.client.AccountServiceClient;
import com.banking.transaction.entity.Transaction;
import com.banking.transaction.entity.TransactionStatus;
import com.banking.transaction.exception.InsufficientBalanceException;
import com.banking.transaction.exception.TransactionException;
import com.banking.transaction.payload.request.TransferRequest;
import com.banking.transaction.payload.response.TransactionEvent;
import com.banking.transaction.payload.response.TransactionHistoryResponse;
import com.banking.transaction.payload.response.TransferResponse;
import com.banking.transaction.producer.TransactionEventProducer;
import com.banking.transaction.repository.TransactionRepository;

@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountServiceClient accountServiceClient;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private TransactionEventProducer eventProducer;

	@Value("${transaction.max.amount:100000}")
	private BigDecimal maxTransactionAmount;

	@Value("${transaction.daily.limit:500000}")
	private BigDecimal dailyLimit;

	@Value("${transaction.lock.timeout.ms:5000}")
	private long lockTimeout;

	private static final String LOCK_KEY_PREFIX = "lock:account:";
	private static final String DAILY_LIMIT_KEY_PREFIX = "daily:limit:account:";

	@Transactional
	public TransferResponse transferFunds(TransferRequest request) {
		log.info("Processing transfer request: from={}, to={}, amount={}", request.getFromAccountId(),
				request.getToAccountId(), request.getAmount());

		// Validate same account
		if (request.getFromAccountId().equals(request.getToAccountId())) {
			throw new TransactionException("Cannot transfer money to the same account");
		}

		// Validate amount limits
		validateAmountLimits(request);

		// Acquire distributed locks for both accounts
		String fromLockKey = LOCK_KEY_PREFIX + request.getFromAccountId();
		String toLockKey = LOCK_KEY_PREFIX + request.getToAccountId();

		Boolean fromLocked = redisTemplate.opsForValue().setIfAbsent(fromLockKey, "locked", lockTimeout,
				TimeUnit.MILLISECONDS);
		Boolean toLocked = redisTemplate.opsForValue().setIfAbsent(toLockKey, "locked", lockTimeout,
				TimeUnit.MILLISECONDS);

		if (Boolean.FALSE.equals(fromLocked) || Boolean.FALSE.equals(toLocked)) {
			throw new TransactionException("System is busy. Please try again.");
		}

		try {
			return executeTransfer(request);
		} finally {
			// Release locks
			redisTemplate.delete(fromLockKey);
			redisTemplate.delete(toLockKey);
		}
	}

	private TransferResponse executeTransfer(TransferRequest request) {
		// Check daily limit
		checkDailyLimit(request.getFromAccountId(), request.getAmount());

		// Validate accounts exist
		if (!accountServiceClient.validateAccount(request.getFromAccountId())) {
			throw new TransactionException("Source account not found");
		}
		if (!accountServiceClient.validateAccount(request.getToAccountId())) {
			throw new TransactionException("Destination account not found");
		}

		// Check balance
		BigDecimal balance = accountServiceClient.getAccountBalance(request.getFromAccountId());
		if (balance.compareTo(request.getAmount()) < 0) {
			throw new InsufficientBalanceException(
					"Insufficient balance. Available: " + balance + ", Required: " + request.getAmount());
		}

		// Create transaction record
		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setToAccountId(request.getToAccountId());
		transaction.setAmount(request.getAmount());
		transaction.setCurrency(request.getCurrency());
		transaction.setDescription(request.getDescription());
		transaction.setStatus(TransactionStatus.PROCESSING);

		transaction = transactionRepository.save(transaction);
		log.info("Transaction created with reference: {}", transaction.getTransactionRef());

		try {
			// Debit from source account
			accountServiceClient.debitAccount(request.getFromAccountId(), request.getAmount(),
					transaction.getTransactionRef());

			// Credit to destination account
			accountServiceClient.creditAccount(request.getToAccountId(), request.getAmount(),
					transaction.getTransactionRef());

			// Update transaction status to COMPLETED
			transaction.setStatus(TransactionStatus.COMPLETED);
			transaction.setCompletedAt(LocalDateTime.now());
			transactionRepository.save(transaction);

			// Update daily limit
			updateDailyLimit(request.getFromAccountId(), request.getAmount());

			// Send event
			TransactionEvent event = new TransactionEvent(transaction.getTransactionRef(), request.getFromAccountId(),
					request.getToAccountId(), request.getAmount(), request.getCurrency(), TransactionStatus.COMPLETED,
					"Transfer completed successfully");
			eventProducer.sendTransactionEvent(event);

			log.info("Transfer completed successfully. Transaction Ref: {}", transaction.getTransactionRef());

			return new TransferResponse(transaction.getId(), transaction.getTransactionRef(), "COMPLETED",
					"Transfer completed successfully");

		} catch (Exception e) {
			log.error("Transfer failed: {}", e.getMessage());
			transaction.setStatus(TransactionStatus.FAILED);
			transaction.setFailureReason(e.getMessage());
			transactionRepository.save(transaction);

			// Send failure event
			TransactionEvent event = new TransactionEvent(transaction.getTransactionRef(), request.getFromAccountId(),
					request.getToAccountId(), request.getAmount(), request.getCurrency(), TransactionStatus.FAILED,
					e.getMessage());
			eventProducer.sendTransactionEvent(event);

			throw new TransactionException("Transfer failed: " + e.getMessage());
		}
	}

	private void validateAmountLimits(TransferRequest request) {
		if (request.getAmount().compareTo(maxTransactionAmount) > 0) {
			throw new TransactionException("Transaction amount exceeds maximum limit of " + maxTransactionAmount);
		}
		if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new TransactionException("Transaction amount must be greater than zero");
		}
	}

	private void checkDailyLimit(UUID accountId, BigDecimal amount) {
		String key = DAILY_LIMIT_KEY_PREFIX + accountId + ":" + LocalDateTime.now().toLocalDate();
		String currentLimitStr = redisTemplate.opsForValue().get(key);

		BigDecimal currentLimit = currentLimitStr != null ? new BigDecimal(currentLimitStr) : BigDecimal.ZERO;
		BigDecimal newLimit = currentLimit.add(amount);

		if (newLimit.compareTo(dailyLimit) > 0) {
			throw new TransactionException(
					"Daily transaction limit exceeded. Current: " + currentLimit + ", Limit: " + dailyLimit);
		}
	}

	private void updateDailyLimit(UUID accountId, BigDecimal amount) {
		String key = DAILY_LIMIT_KEY_PREFIX + accountId + ":" + LocalDateTime.now().toLocalDate();
		redisTemplate.opsForValue().increment(key, amount.doubleValue());
		redisTemplate.expire(key, 1, TimeUnit.DAYS);
	}

	public TransactionHistoryResponse getTransactionByReference(String transactionRef) {
		log.info("Fetching transaction by reference: {}", transactionRef);
		Transaction transaction = transactionRepository.findByTransactionRef(transactionRef)
				.orElseThrow(() -> new TransactionException("Transaction not found with reference: " + transactionRef));
		return TransactionHistoryResponse.fromEntity(transaction);
	}

	public List<TransactionHistoryResponse> getTransactionHistory(UUID accountId) {
		log.info("Fetching transaction history for account: {}", accountId);
		return transactionRepository.findAllByAccountId(accountId).stream()
				.map(t -> TransactionHistoryResponse.fromEntityWithType(t, accountId)).collect(Collectors.toList());
	}

	public Page<TransactionHistoryResponse> getTransactionHistoryPaginated(UUID accountId, int page, int size) {
		log.info("Fetching paginated transaction history for account: {}, page: {}, size: {}", accountId, page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Transaction> transactionPage = transactionRepository
				.findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(accountId, accountId, pageable);
		return transactionPage.map(t -> TransactionHistoryResponse.fromEntityWithType(t, accountId));
	}

	public List<TransactionHistoryResponse> getTransactionsByStatus(TransactionStatus status) {
		log.info("Fetching transactions by status: {}", status);
		return transactionRepository.findByStatus(status).stream().map(TransactionHistoryResponse::fromEntity)
				.collect(Collectors.toList());
	}

	public BigDecimal getTotalDailyDebit(UUID accountId) {
		String key = DAILY_LIMIT_KEY_PREFIX + accountId + ":" + LocalDateTime.now().toLocalDate();
		String value = redisTemplate.opsForValue().get(key);
		return value != null ? new BigDecimal(value) : BigDecimal.ZERO;
	}

	@Transactional
	public void cleanupFailedTransactions() {
		log.info("Cleaning up old failed transactions");
		LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
		long count = transactionRepository.countByStatusAndCreatedAtBefore(TransactionStatus.FAILED, cutoffDate);
		log.info("Found {} failed transactions older than 30 days", count);
	}
}
package com.banking.transaction.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.banking.transaction.payload.response.TransactionEvent;

@Component
public class TransactionEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(TransactionEventConsumer.class);

	@KafkaListener(topics = "transaction-events", groupId = "transaction-group")
	public void consumeTransactionEvent(TransactionEvent event) {
		log.info("Received transaction event from Kafka: {}", event);
		// Process event - could be used for notifications, analytics, etc.
		handleTransactionEvent(event);
	}

	private void handleTransactionEvent(TransactionEvent event) {
		switch (event.getStatus()) {
		case COMPLETED:
			log.info("Transaction {} completed successfully", event.getTransactionRef());
			break;
		case FAILED:
			log.warn("Transaction {} failed: {}", event.getTransactionRef(), event.getMessage());
			break;
		case PENDING:
			log.info("Transaction {} is pending", event.getTransactionRef());
			break;
		default:
			log.debug("Transaction {} status: {}", event.getTransactionRef(), event.getStatus());
		}
	}
}
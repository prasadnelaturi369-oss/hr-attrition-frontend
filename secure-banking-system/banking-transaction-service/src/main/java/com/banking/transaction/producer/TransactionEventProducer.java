package com.banking.transaction.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.banking.transaction.payload.response.TransactionEvent;

@Component
public class TransactionEventProducer {

	private static final Logger log = LoggerFactory.getLogger(TransactionEventProducer.class);

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	private static final String TOPIC = "transaction-events";

	public void sendTransactionEvent(TransactionEvent event) {
		log.info("Sending transaction event to Kafka: {}", event);
		try {
			kafkaTemplate.send(TOPIC, event.getTransactionRef(), event);
			log.info("Transaction event sent successfully");
		} catch (Exception e) {
			log.error("Failed to send transaction event: {}", e.getMessage());
		}
	}
}
package com.banking.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Prasad Nelaturi
 * @contact prasadnelaturi@thestackly.com
 * @since 2026-05-26 TO 2026-06-01
 * @project Eureka Server
 * @organization Stackly
 * 
 * Assigned by : Nellaiappan V
 * 
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableRetry
@EnableScheduling
public class BankingTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingTransactionServiceApplication.class, args);
		System.out.println("Transaction Service........");
	}

}

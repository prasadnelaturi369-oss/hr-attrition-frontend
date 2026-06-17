package com.banking.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Prasad Nelaturi
 * @contact prasadnelaturi@thestackly.com
 * @since 2026-05-26 TO 2026-06-01
 * @project Eureka Server
 * @organization Stackly
 * 
 *               Assigned by : Nellaiappan V
 * 
 */

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@EnableDiscoveryClient
@EnableScheduling
public class BankingAuditServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAuditServiceApplication.class, args);
		System.out.println("Audit Service........");
	}

}

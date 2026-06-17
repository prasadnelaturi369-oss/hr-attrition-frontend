package com.banking.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
public class AccountServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
		System.out.println("Account Service........");
	}
}
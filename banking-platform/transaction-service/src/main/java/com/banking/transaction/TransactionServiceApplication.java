package com.banking.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
*
* @author Prasad Nelaturi
* @contact prasadnelaturi@thestackly.com
* @since 2026-05-21 TO 2026-05-25
* @project Transaction Service
* @organization Stackly
* 
* Assigned by : Nellaiappan V
* 
*/

@SpringBootApplication
@EnableDiscoveryClient
public class TransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
		System.out.println("Transaction Service........");
	}

}

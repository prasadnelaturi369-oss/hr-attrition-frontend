package com.banking.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
*
* @author Prasad Nelaturi
* @contact prasadnelaturi@thestackly.com
* @since 2026-05-21 TO 2026-05-25
* @project Customer Service
* @organization Stackly
* 
* Assigned by : Nellaiappan V
* 
*/

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
		System.out.println("Customer Service........");
	}

}

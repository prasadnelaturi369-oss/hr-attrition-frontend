package com.banking.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
*
* @author Prasad Nelaturi
* @contact prasadnelaturi@thestackly.com
* @since 2026-05-21 TO 2026-05-25
* @project Api Gateway
* @organization Stackly
* 
* Assigned by : Nellaiappan V
* 
*/

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("Api Gateway........");
	}

}

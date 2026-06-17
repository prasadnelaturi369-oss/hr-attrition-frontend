package com.banking.service_registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *
 * @author Prasad Nelaturi
 * @contact prasadnelaturi@thestackly.com
 * @since 2026-05-21 TO 2026-05-25
 * @project Service Registry
 * @organization Stackly
 * 
 * Assigned by : Nellaiappan V
 * 
 */

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryApplication.class, args);
		System.out.println("Service Registry........");
	}

}

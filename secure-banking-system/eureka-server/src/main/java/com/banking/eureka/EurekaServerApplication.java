package com.banking.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
*
* @author Prasad Nelaturi
* @contact prasadnelaturi@thestackly.com
* @since 2026-05-26 TO 2026-06-02
* @project Eureka Server
* @organization Stackly
* 
* Assigned by : Nellaiappan V
* 
*/

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
		System.out.println("Eureka Server........");
	}
}
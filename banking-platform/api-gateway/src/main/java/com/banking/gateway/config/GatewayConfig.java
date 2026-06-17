package com.banking.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("customer-service",
						r -> r.path("/api/auth/**", "/api/customers/**").uri("lb://CUSTOMER-SERVICE"))
				.route("account-service", r -> r.path("/api/accounts/**").uri("lb://ACCOUNT-SERVICE"))
				.route("transaction-service", r -> r.path("/api/transactions/**").uri("lb://TRANSACTION-SERVICE"))
				.route("notification-service", r -> r.path("/api/notifications/**").uri("lb://NOTIFICATION-SERVICE"))
				.build();
	}
}
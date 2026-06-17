package com.banking.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.banking.gateway.filter.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route("auth-service",
				r -> r.path("/api/auth/**").filters(f -> f.filter(jwtAuthenticationFilter)).uri("lb://AUTH-SERVICE"))
				.route("account-service",
						r -> r.path("/api/accounts/**").filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://ACCOUNT-SERVICE"))
				.route("transaction-service",
						r -> r.path("/api/transactions/**").filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://TRANSACTION-SERVICE"))
				.route("audit-service", r -> r.path("/api/audit/**").filters(f -> f.filter(jwtAuthenticationFilter))
						.uri("lb://AUDIT-SERVICE"))
				.build();
	}
}
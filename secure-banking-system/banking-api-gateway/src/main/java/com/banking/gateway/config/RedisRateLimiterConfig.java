package com.banking.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import reactor.core.publisher.Mono;

@Configuration
public class RedisRateLimiterConfig {

	@Bean
	@Primary
	public KeyResolver apiKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
	}

	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> {
			String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
			if (userId != null && !userId.isEmpty()) {
				return Mono.just(userId);
			}
			return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
		};
	}
}
package com.banking.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

	@Value("${jwt.secret}")
	private String jwtSecret;

	private final List<String> openEndpoints = List.of("/api/auth/register", "/api/auth/login", "/api/auth/validate",
			"/actuator/health", "/swagger-ui", "/api-docs", "/health", "/routes");

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();

		// Check if endpoint is public
		Predicate<String> isOpenEndpoint = endpoint -> openEndpoints.stream().anyMatch(path::contains);

		if (isOpenEndpoint.test(path)) {
			return chain.filter(exchange);
		}

		// Check for Authorization header
		if (!request.getHeaders().containsKey("Authorization")) {
			return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
		}

		String authHeader = request.getHeaders().getFirst("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return onError(exchange, "Invalid authorization header format", HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7);

		try {
			// Validate JWT token
			Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

			String userId = claims.getSubject();
			String username = claims.get("username", String.class);
			String email = claims.get("email", String.class);
			String role = claims.get("role", String.class);

			// Add user details to headers for downstream services
			ServerHttpRequest mutatedRequest = request.mutate().header("X-User-Id", userId)
					.header("X-Username", username != null ? username : "")
					.header("X-Email", email != null ? email : "").header("X-User-Role", role != null ? role : "")
					.build();

			return chain.filter(exchange.mutate().request(mutatedRequest).build());

		} catch (Exception e) {
			return onError(exchange, "Invalid or expired token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().add("Content-Type", "application/json");
		String body = String.format("{\"error\":\"%s\",\"status\":%d}", message, status.value());
		return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
	}
}
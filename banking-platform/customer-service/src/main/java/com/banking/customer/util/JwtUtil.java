package com.banking.customer.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final SecretKey secretKey = Keys.hmacShaKeyFor(
			"your-secret-key-for-jwt-must-be-at-least-32-characters-long-256-bits".getBytes(StandardCharsets.UTF_8));
	private final long EXPIRATION_TIME = 86400000;

	public String generateToken(String userId, String email, String role) {
		return Jwts.builder().subject(userId).claim("email", email).claim("role", role).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(secretKey).compact();
	}
}
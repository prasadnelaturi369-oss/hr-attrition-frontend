package com.gateway.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gateway.exception.MissingApiKeyException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

	@Value("${api.key.required}")
	private String validApiKey;

	private static final String API_KEY_HEADER = "X-API-KEY";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestPath = request.getRequestURI();

		// Skip API key validation for Swagger UI endpoints
		if (isSwaggerEndpoint(requestPath)) {
			filterChain.doFilter(request, response);
			return;
		}

		// Validate API Key for all /api endpoints
		if (requestPath.startsWith("/api")) {
			String apiKey = request.getHeader(API_KEY_HEADER);
			if (apiKey == null || !apiKey.equals(validApiKey)) {
				throw new MissingApiKeyException("Missing or invalid X-API-KEY header. Access denied.");
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean isSwaggerEndpoint(String path) {
		return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui.html")
				|| path.startsWith("/api-docs") || path.startsWith("/h2-console");
	}
}
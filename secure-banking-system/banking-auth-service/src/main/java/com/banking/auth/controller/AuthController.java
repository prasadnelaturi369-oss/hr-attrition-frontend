package com.banking.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.auth.entity.User;
import com.banking.auth.payload.request.LoginRequest;
import com.banking.auth.payload.request.RegisterRequest;
import com.banking.auth.payload.response.AuthResponse;
import com.banking.auth.security.JwtTokenProvider;
import com.banking.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final JwtTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	public AuthController(AuthService authService, JwtTokenProvider tokenProvider,
			AuthenticationManager authenticationManager) {
		super();
		this.authService = authService;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		User user = authService.register(request);

		// Auto-login after registration
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);

		return ResponseEntity
				.ok(new AuthResponse(jwt, "Bearer", user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);

		User user = authService.getUserByUsernameOrEmail(request.getUsernameOrEmail());

		return ResponseEntity
				.ok(new AuthResponse(jwt, "Bearer", user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestParam(required = false) String token) {
		if (token == null || token.isEmpty()) {
			return ResponseEntity.badRequest().body("Token is required");
		}

		boolean isValid = tokenProvider.validateToken(token);

		if (isValid) {
			return ResponseEntity.ok(java.util.Collections.singletonMap("valid", true));
		} else {
			return ResponseEntity.status(401).body(java.util.Collections.singletonMap("valid", false));
		}
	}
}
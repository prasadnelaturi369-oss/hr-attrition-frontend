package com.rbac.controller;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbac.entity.User;
import com.rbac.payload.request.LoginRequest;
import com.rbac.payload.request.RegisterRequest;
import com.rbac.payload.response.ApiResponse;
import com.rbac.payload.response.AuthResponse;
import com.rbac.service.JwtService;
import com.rbac.service.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserServiceImpl userService;
	private final JwtService jwtService;

	public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userService,
			JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = (User) authentication.getPrincipal();
		String token = jwtService.generateToken(user);

		// In AuthController.java - without builder
		AuthResponse response = AuthResponse.of(token, user.getId(), user.getUsername(), user.getEmail(),
				user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()),
				user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toSet()));

		return ApiResponse.success(response, "Login successful");
	}

	@PostMapping("/register")
	public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		var userResponse = userService.registerUser(request);

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = (User) authentication.getPrincipal();
		String token = jwtService.generateToken(user);

		// In AuthController.java - without builder
		AuthResponse response = AuthResponse.of(token, user.getId(), user.getUsername(), user.getEmail(),
				user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()),
				user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toSet()));

		return ApiResponse.success(response, "Registration successful");
	}
}
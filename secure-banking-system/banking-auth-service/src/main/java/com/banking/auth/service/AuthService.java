package com.banking.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.auth.entity.User;
import com.banking.auth.payload.request.LoginRequest;
import com.banking.auth.payload.request.RegisterRequest;
import com.banking.auth.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	@Transactional
	public User register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setPhoneNumber(request.getPhoneNumber());

		return userRepository.save(user);
	}

	public Authentication login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));

		User user = userRepository.findByUsername(request.getUsernameOrEmail()).orElseGet(() -> userRepository
				.findByEmail(request.getUsernameOrEmail()).orElseThrow(() -> new RuntimeException("User not found")));

		user.setLastLogin(LocalDateTime.now());
		userRepository.save(user);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	public User getUserByUsernameOrEmail(String usernameOrEmail) {
		return userRepository.findByUsername(usernameOrEmail).orElseGet(() -> userRepository
				.findByEmail(usernameOrEmail).orElseThrow(() -> new RuntimeException("User not found")));
	}
}
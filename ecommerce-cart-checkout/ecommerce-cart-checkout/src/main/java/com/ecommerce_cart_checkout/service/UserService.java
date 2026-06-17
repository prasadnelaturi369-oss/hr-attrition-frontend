package com.ecommerce_cart_checkout.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_cart_checkout.entity.User;
import com.ecommerce_cart_checkout.exception.BusinessException;
import com.ecommerce_cart_checkout.payload.request.UserRequest;
import com.ecommerce_cart_checkout.payload.response.UserResponse;
import com.ecommerce_cart_checkout.repository.UserRepository;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {

		log.info("Creating user with email: {}", request.getEmail());

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {

			throw new BusinessException("User already exists with email: " + request.getEmail());
		}

		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());

		User savedUser = userRepository.save(user);

		log.info("User created successfully with id: {}", savedUser.getId());

		return mapToResponse(savedUser);
	}

	public UserResponse getUserById(Long id) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new BusinessException("User not found with id: " + id));

		return mapToResponse(user);
	}

	private UserResponse mapToResponse(User user) {

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
	}
}
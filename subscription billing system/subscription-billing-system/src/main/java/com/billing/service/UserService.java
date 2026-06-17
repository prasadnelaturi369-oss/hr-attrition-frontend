package com.billing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billing.entity.User;
import com.billing.exception.BusinessException;
import com.billing.exception.ResourceNotFoundException;
import com.billing.payload.request.UserRequest;
import com.billing.payload.response.UserResponse;
import com.billing.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final AuditService auditService;

	public UserService(UserRepository userRepository, AuditService auditService) {
		this.userRepository = userRepository;
		this.auditService = auditService;
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {
		log.info("Creating new user: {}", request.getEmail());

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException("User with email " + request.getEmail() + " already exists");
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setPhone(request.getPhone());
		user.setCompanyName(request.getCompanyName());
		user.setBillingAddress(request.getBillingAddress());
		user.setRole(request.getRole() != null ? request.getRole() : "CUSTOMER");

		User savedUser = userRepository.save(user);

		auditService.log("User", savedUser.getId(), "CREATED", null, savedUser.getEmail(), "User created");

		return mapToResponse(savedUser);
	}

	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return mapToResponse(user);
	}

	public Page<UserResponse> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable).map(this::mapToResponse);
	}

	public Page<UserResponse> getUsersByRole(String role, Pageable pageable) {
		return userRepository.findByRole(role, pageable).map(this::mapToResponse);
	}

	@Transactional
	public UserResponse updateUser(Long id, UserRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		String oldEmail = user.getEmail();

		if (request.getFirstName() != null)
			user.setFirstName(request.getFirstName());
		if (request.getLastName() != null)
			user.setLastName(request.getLastName());
		if (request.getPhone() != null)
			user.setPhone(request.getPhone());
		if (request.getCompanyName() != null)
			user.setCompanyName(request.getCompanyName());
		if (request.getBillingAddress() != null)
			user.setBillingAddress(request.getBillingAddress());

		User updatedUser = userRepository.save(user);

		auditService.log("User", updatedUser.getId(), "UPDATED", oldEmail, updatedUser.getEmail(), "User updated");

		return mapToResponse(updatedUser);
	}

	@Transactional
	public void deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setStatus("INACTIVE");
		userRepository.save(user);

		auditService.log("User", user.getId(), "DELETED", user.getEmail(), null, "User deactivated");
	}

	private UserResponse mapToResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setFullName(user.getFullName());
		response.setPhone(user.getPhone());
		response.setCompanyName(user.getCompanyName());
		response.setBillingAddress(user.getBillingAddress());
		response.setRole(user.getRole());
		response.setStatus(user.getStatus());
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());
		return response;
	}
}
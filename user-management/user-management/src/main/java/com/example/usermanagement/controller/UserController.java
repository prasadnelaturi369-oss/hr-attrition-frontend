package com.example.usermanagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagement.model.UserEntity;
import com.example.usermanagement.payload.request.UserRequest;
import com.example.usermanagement.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRequest request) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (request.getName() == null || request.getName().isBlank() || request.getEmail() == null
					|| request.getEmail().isBlank() || request.getRole() == null || request.getRole().isBlank()) {

				response.put("status", "failure");
				response.put("message", "Fill all the required fields (name, email, role)");
				return ResponseEntity.badRequest().body(response);
			}

			if (userRepository.existsByEmail(request.getEmail())) {
				response.put("status", "failure");
				response.put("message", "Email already exists: " + request.getEmail());
				return ResponseEntity.badRequest().body(response);
			}

			UserEntity user = new UserEntity();
			user.setName(request.getName());
			user.setEmail(request.getEmail());
			user.setRole(request.getRole());

			UserEntity savedUser = userRepository.save(user);
			logger.info("User created successfully with id:", savedUser.getId());

			response.put("status", "success");
			response.put("message", "User created successfully");
			response.put("data", savedUser);
			return ResponseEntity.status(201).body(response);
		} catch (Exception e) {
			logger.error("Error creating user", e);
			response.put("status", "failure");
			response.put("message", "Internal server error");
			return ResponseEntity.status(500).body(response);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<UserEntity> user = userRepository.findById(id);
			if (user.isEmpty()) {
				response.put("status", "failure");
				response.put("message", "User not found with ID: " + id);
				return ResponseEntity.status(404).body(response);
			}

			if (request.getName() == null || request.getName().isBlank() || request.getEmail() == null
					|| request.getEmail().isBlank() || request.getRole() == null || request.getRole().isBlank()) {
				response.put("status", "failure");
				response.put("message", "Fill all the required fields (name, email, role)");
				return ResponseEntity.badRequest().body(response);
			}

			UserEntity existingUser = user.get();
			if (!existingUser.getEmail().equals(request.getEmail())
					&& userRepository.existsByEmail(request.getEmail())) {
				response.put("status", "failure");
				response.put("message", "Email already exists: " + request.getEmail());
				return ResponseEntity.badRequest().body(response);
			}

			existingUser.setName(request.getName());
			existingUser.setEmail(request.getEmail());
			existingUser.setRole(request.getRole());

			UserEntity updatedUser = userRepository.save(existingUser);
			logger.info("User updated successfully with id:", id);

			response.put("status", "success");
			response.put("message", "User updated successfully: " + id);
			response.put("data", updatedUser);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Internal server error while updating user id:", id, e);
			response.put("status", "failure");
			response.put("message", "Internal server error");
			return ResponseEntity.status(500).body(response);
		}
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllUsers() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<UserEntity> users = userRepository.findAll();

			List<Map<String, Object>> userList = new ArrayList<>();

			for (UserEntity user : users) {
				Map<String, Object> userMap = new HashMap<>();
				userMap.put("id", user.getId());
				userMap.put("name", user.getName());
				userMap.put("email", user.getEmail());
				userMap.put("role", user.getRole());
				userList.add(userMap);
			}
			response.put("data", userList);

			response.put("status", "success");
			response.put("message", "Users retrieved successfully");
			response.put("data", userList);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error fetching users", e);
			response.put("status", "failure");
			response.put("message", "Internal server error");
			return ResponseEntity.status(500).body(response);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<UserEntity> users = userRepository.findAll();
			UserEntity foundUser = null;

			for (UserEntity user : users) {
				if (user.getId().equals(id)) {
					foundUser = user;
					break;
				}
			}

			if (foundUser == null) {
				response.put("status", "failure");
				response.put("message", "User not found with ID: " + id);
				return ResponseEntity.status(404).body(response);
			}

			response.put("status", "success");
			response.put("message", "User retrieved successfully");
			response.put("data", foundUser);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error fetching user by id: {}", id, e);
			response.put("status", "failure");
			response.put("message", "Internal server error");
			return ResponseEntity.status(500).body(response);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (!userRepository.existsById(id)) {
				response.put("status", "failure");
				response.put("message", "User not found with ID: " + id);
				return ResponseEntity.status(404).body(response);
			}
			userRepository.deleteById(id);
			logger.info("User deleted successfully with id: {}", id);
			response.put("status", "success");
			response.put("message", "User deleted successfully: " + id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error deleting user id: {}", id, e);
			response.put("status", "failure");
			response.put("message", "Internal server error");
			return ResponseEntity.status(500).body(response);
		}
	}
}

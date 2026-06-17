package com.gateway.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	// In-memory storage for users (simulating database)
	private final Map<Long, Map<String, Object>> userStorage = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(4); // Start from 4 since we have 3 default users

	public UserService() {
		// Initialize with default users
		userStorage.put(1L, createUserMap(1L, "John Doe", "john@example.com", "ACTIVE"));
		userStorage.put(2L, createUserMap(2L, "Jane Smith", "jane@example.com", "ACTIVE"));
		userStorage.put(3L, createUserMap(3L, "Bob Johnson", "bob@example.com", "INACTIVE"));
	}

	public Map<String, Object> getUsers() {
		log.debug("Fetching users from User Service");

		Map<String, Object> response = new HashMap<>();
		response.put("service", "User Service");
		response.put("status", "success");
		response.put("timestamp", System.currentTimeMillis());
		response.put("count", userStorage.size());
		response.put("data", userStorage.values());

		return response;
	}

	public Map<String, Object> addUser(Map<String, Object> userRequest) {
		log.debug("Adding new user to User Service");

		Long newId = idGenerator.getAndIncrement();
		String name = (String) userRequest.get("name");
		String email = (String) userRequest.get("email");
		String status = (String) userRequest.getOrDefault("status", "ACTIVE");

		Map<String, Object> newUser = createUserMap(newId, name, email, status);
		userStorage.put(newId, newUser);

		Map<String, Object> response = new HashMap<>();
		response.put("service", "User Service");
		response.put("status", "success");
		response.put("message", "User created successfully");
		response.put("data", newUser);

		return response;
	}

	private Map<String, Object> createUserMap(Long id, String name, String email, String status) {
		Map<String, Object> user = new HashMap<>();
		user.put("id", id);
		user.put("name", name);
		user.put("email", email);
		user.put("status", status);
		return user;
	}
}
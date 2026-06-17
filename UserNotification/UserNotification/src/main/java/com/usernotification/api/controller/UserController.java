package com.usernotification.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usernotification.api.payload.request.UserRequest;
import com.usernotification.api.payload.respose.UserResponse;
import com.usernotification.api.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping
	public UserResponse create(@RequestBody UserRequest request) {
		return service.createUser(request);
	}

	@GetMapping("/{id}")
	public UserResponse get(@PathVariable Long id) {
		return service.getUser(id);
	}
}

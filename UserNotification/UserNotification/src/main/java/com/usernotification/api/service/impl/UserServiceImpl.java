package com.usernotification.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usernotification.api.model.User;
import com.usernotification.api.payload.request.UserRequest;
import com.usernotification.api.payload.respose.UserResponse;
import com.usernotification.api.repository.UserRepository;
import com.usernotification.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repo;

	@Override
	public UserResponse createUser(UserRequest request) {

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());

		User saved = repo.save(user);

		UserResponse res = new UserResponse();
		res.setId(saved.getId());
		res.setName(saved.getName());
		res.setEmail(saved.getEmail());
		res.setPhone(saved.getPhone());

		return res;
	}

	@Override
	public UserResponse getUser(Long id) {

		User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		UserResponse res = new UserResponse();
		res.setId(user.getId());
		res.setName(user.getName());
		res.setEmail(user.getEmail());
		res.setPhone(user.getPhone());

		return res;
	}
}
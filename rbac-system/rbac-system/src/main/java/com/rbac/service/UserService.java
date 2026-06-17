package com.rbac.service;

import com.rbac.entity.User;
import com.rbac.payload.request.AssignRoleRequest;
import com.rbac.payload.request.RegisterRequest;
import com.rbac.payload.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
	UserResponse registerUser(RegisterRequest request);

	UserResponse getUserById(Long id);

	List<UserResponse> getAllUsers();

	UserResponse updateUser(Long id, RegisterRequest request);

	void deleteUser(Long id);

	UserResponse assignRole(AssignRoleRequest request);

	UserResponse removeRole(Long userId, Long roleId);

	User getCurrentUser();
}
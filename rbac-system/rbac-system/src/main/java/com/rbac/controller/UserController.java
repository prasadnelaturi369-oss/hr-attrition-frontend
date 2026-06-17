package com.rbac.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbac.payload.request.AssignRoleRequest;
import com.rbac.payload.request.RegisterRequest;
import com.rbac.payload.response.ApiResponse;
import com.rbac.payload.response.UserResponse;
import com.rbac.service.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserServiceImpl userService;

	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ApiResponse<UserResponse> getCurrentUser() {
		UserResponse user = userService.getUserById(userService.getCurrentUser().getId());
		return ApiResponse.success(user, "Current user retrieved successfully");
	}

	@GetMapping
	@PreAuthorize("hasAuthority('USER_VIEW')")
	public ApiResponse<java.util.List<UserResponse>> getAllUsers() {
		return ApiResponse.success(userService.getAllUsers(), "Users retrieved successfully");
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('USER_VIEW') or @userServiceImpl.getCurrentUser().getId() == #id")
	public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
		return ApiResponse.success(userService.getUserById(id), "User retrieved successfully");
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('USER_UPDATE') or @userServiceImpl.getCurrentUser().getId() == #id")
	public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterRequest request) {
		return ApiResponse.success(userService.updateUser(id, request), "User updated successfully");
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('USER_DELETE')")
	public ApiResponse<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ApiResponse.success(null, "User deleted successfully");
	}

	@PostMapping("/assign-role")
	@PreAuthorize("hasAuthority('ROLE_ASSIGN')")
	public ApiResponse<UserResponse> assignRole(@Valid @RequestBody AssignRoleRequest request) {
		return ApiResponse.success(userService.assignRole(request), "Role assigned successfully");
	}

	@DeleteMapping("/{userId}/roles/{roleId}")
	@PreAuthorize("hasAuthority('ROLE_REMOVE')")
	public ApiResponse<UserResponse> removeRole(@PathVariable Long userId, @PathVariable Long roleId) {
		return ApiResponse.success(userService.removeRole(userId, roleId), "Role removed successfully");
	}
}
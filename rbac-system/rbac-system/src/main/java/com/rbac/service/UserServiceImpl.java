package com.rbac.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbac.entity.Role;
import com.rbac.entity.User;
import com.rbac.exception.DuplicateResourceException;
import com.rbac.exception.ResourceNotFoundException;
import com.rbac.payload.request.AssignRoleRequest;
import com.rbac.payload.request.RegisterRequest;
import com.rbac.payload.response.UserResponse;
import com.rbac.repository.RoleRepository;
import com.rbac.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public UserResponse registerUser(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new DuplicateResourceException("Username already exists: " + request.getUsername());
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateResourceException("Email already exists: " + request.getEmail());
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEnabled(true);

		// Assign default role
		Role defaultRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
		user.getRoles().add(defaultRole);

		User savedUser = userRepository.save(user);
		return mapToResponse(savedUser);
	}

	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return mapToResponse(user);
	}

	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserResponse updateUser(Long id, RegisterRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		if (request.getFirstName() != null)
			user.setFirstName(request.getFirstName());
		if (request.getLastName() != null)
			user.setLastName(request.getLastName());
		if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
			if (userRepository.existsByEmail(request.getEmail())) {
				throw new DuplicateResourceException("Email already exists: " + request.getEmail());
			}
			user.setEmail(request.getEmail());
		}

		User updatedUser = userRepository.save(user);
		return mapToResponse(updatedUser);
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public UserResponse assignRole(AssignRoleRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

		Role role = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

		user.getRoles().add(role);
		User updatedUser = userRepository.save(user);
		return mapToResponse(updatedUser);
	}

	@Override
	@Transactional
	public UserResponse removeRole(Long userId, Long roleId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

		user.getRoles().remove(role);
		User updatedUser = userRepository.save(user);
		return mapToResponse(updatedUser);
	}

	@Override
	public User getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}

	private UserResponse mapToResponse(User user) {
		return UserResponse.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).enabled(user.getEnabled())
				.roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
				.createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
	}
}
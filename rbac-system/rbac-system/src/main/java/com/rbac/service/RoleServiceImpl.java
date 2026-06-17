package com.rbac.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbac.entity.Permission;
import com.rbac.entity.Role;
import com.rbac.payload.request.CreateRoleRequest;
import com.rbac.payload.response.RoleResponse;
import com.rbac.repository.PermissionRepository;
import com.rbac.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public RoleResponse createRole(CreateRoleRequest request) {

		if (roleRepository.existsByName(request.getName())) {
			throw new RuntimeException("Role already exists");
		}

		Role role = new Role();
		role.setName(request.getName());
		role.setDescription(request.getDescription());

		if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {

			Set<Permission> permissions = new HashSet<>();

			for (Long permissionId : request.getPermissionIds()) {

				Permission permission = permissionRepository.findById(permissionId)
						.orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));

				permissions.add(permission);
			}

			role.setPermissions(permissions);
		}

		Role savedRole = roleRepository.save(role);

		return mapToResponse(savedRole);
	}

	@Override
	public RoleResponse getRoleById(Long id) {

		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		return mapToResponse(role);
	}

	@Override
	public List<RoleResponse> getAllRoles() {

		return roleRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public void deleteRole(Long id) {

		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		roleRepository.delete(role);
	}

	@Override
	public Role getRoleByName(String name) {

		return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
	}

	private RoleResponse mapToResponse(Role role) {

		Set<String> permissions = role.getPermissions().stream().map(permission -> permission.getName())
				.collect(Collectors.toSet());

		return RoleResponse.builder().id(role.getId()).name(role.getName()).description(role.getDescription())
				.permissions(permissions).build();
	}

	@Override
	public RoleResponse updateRole(Long id, CreateRoleRequest request) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public RoleResponse assignPermission(Long roleId, Long permissionId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public RoleResponse removePermission(Long roleId, Long permissionId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}

package com.rbac.service;

import java.util.List;

import com.rbac.entity.Role;
import com.rbac.payload.request.CreateRoleRequest;
import com.rbac.payload.response.RoleResponse;

public interface RoleService {
	RoleResponse createRole(CreateRoleRequest request);

	RoleResponse getRoleById(Long id);

	List<RoleResponse> getAllRoles();

	RoleResponse updateRole(Long id, CreateRoleRequest request);

	void deleteRole(Long id);

	RoleResponse assignPermission(Long roleId, Long permissionId);

	RoleResponse removePermission(Long roleId, Long permissionId);

	Role getRoleByName(String name);
}
package com.rbac.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbac.payload.request.CreateRoleRequest;
import com.rbac.payload.response.RoleResponse;
import com.rbac.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping
	public ResponseEntity<RoleResponse> createRole(@RequestBody CreateRoleRequest request) {

		return ResponseEntity.ok(roleService.createRole(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {

		return ResponseEntity.ok(roleService.getRoleById(id));
	}

	@GetMapping
	public ResponseEntity<List<RoleResponse>> getAllRoles() {

		return ResponseEntity.ok(roleService.getAllRoles());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable Long id) {

		roleService.deleteRole(id);

		return ResponseEntity.ok("Role deleted successfully");
	}
}
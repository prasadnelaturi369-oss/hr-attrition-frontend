package com.rbac.payload.response;

import java.util.Set;

public class RoleResponse {
	private Long id;
	private String name;
	private String description;
	private Set<String> permissions;

	public RoleResponse() {
	}

	public RoleResponse(Long id, String name, String description, Set<String> permissions) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.permissions = permissions;
	}

	public static RoleResponseBuilder builder() {
		return new RoleResponseBuilder();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	// Setters
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	// Builder class
	public static class RoleResponseBuilder {
		private Long id;
		private String name;
		private String description;
		private Set<String> permissions;

		RoleResponseBuilder() {
		}

		public RoleResponseBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public RoleResponseBuilder name(String name) {
			this.name = name;
			return this;
		}

		public RoleResponseBuilder description(String description) {
			this.description = description;
			return this;
		}

		public RoleResponseBuilder permissions(Set<String> permissions) {
			this.permissions = permissions;
			return this;
		}

		public RoleResponse build() {
			return new RoleResponse(id, name, description, permissions);
		}
	}
}
package com.rbac.payload.response;

import java.time.LocalDateTime;
import java.util.Set;

public class UserResponse {
	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private Boolean enabled;
	private Set<String> roles;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Default constructor
	public UserResponse() {
	}

	// All-args constructor
	public UserResponse(Long id, String username, String email, String firstName, String lastName, Boolean enabled,
			Set<String> roles, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enabled = enabled;
		this.roles = roles;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Builder pattern
	public static UserResponseBuilder builder() {
		return new UserResponseBuilder();
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	// Setters
	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	// Builder class
	public static class UserResponseBuilder {
		private Long id;
		private String username;
		private String email;
		private String firstName;
		private String lastName;
		private Boolean enabled;
		private Set<String> roles;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

		UserResponseBuilder() {
		}

		public UserResponseBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public UserResponseBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserResponseBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserResponseBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserResponseBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public UserResponseBuilder enabled(Boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public UserResponseBuilder roles(Set<String> roles) {
			this.roles = roles;
			return this;
		}

		public UserResponseBuilder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public UserResponseBuilder updatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public UserResponse build() {
			return new UserResponse(id, username, email, firstName, lastName, enabled, roles, createdAt, updatedAt);
		}
	}
}
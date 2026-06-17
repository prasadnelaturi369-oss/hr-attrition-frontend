package com.banking.customer.payload.response;

public class AuthResponse {
	private String token;
	private String type;
	private String id;
	private String email;
	private String role;

	public AuthResponse(String token, String type, String id, String email, String role) {
		this.token = token;
		this.type = type;
		this.id = id;
		this.email = email;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}
}
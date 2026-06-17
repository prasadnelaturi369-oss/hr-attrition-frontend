package com.banking.auth.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.banking.auth.entity.User;

public class UserPrincipal implements UserDetails {

	private final UUID id;
	private final String username;
	private final String email;
	private final String password;
	private final String role;
	private final boolean active;

	public UserPrincipal(UUID id, String username, String email, String password, String role, boolean active) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.active = active;
	}

	public static UserPrincipal create(User user) {
		return new UserPrincipal(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(),
				user.getActive());
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return active;
	}

	@Override
	public boolean isAccountNonLocked() {
		return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}
}
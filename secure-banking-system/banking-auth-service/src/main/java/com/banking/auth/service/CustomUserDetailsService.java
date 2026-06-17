package com.banking.auth.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banking.auth.entity.User;
import com.banking.auth.repository.UserRepository;
import com.banking.auth.security.UserPrincipal;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(usernameOrEmail)
				.orElseGet(() -> userRepository.findByEmail(usernameOrEmail).orElseThrow(
						() -> new UsernameNotFoundException("User not found with username/email: " + usernameOrEmail)));

		return UserPrincipal.create(user);
	}

	public UserDetails loadUserById(UUID id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
		return UserPrincipal.create(user);
	}
}
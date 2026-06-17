package com.billing.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Page<User> findByRole(String role, Pageable pageable);

	Page<User> findByStatus(String status, Pageable pageable);

	long countByStatus(String status);

	boolean existsByEmail(String email);
}
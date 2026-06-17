package com.usernotification.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usernotification.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
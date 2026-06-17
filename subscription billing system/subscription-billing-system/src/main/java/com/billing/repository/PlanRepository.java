package com.billing.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	Optional<Plan> findByName(String name);

	Page<Plan> findByStatus(String status, Pageable pageable);

	Page<Plan> findByBillingCycle(String billingCycle, Pageable pageable);

	boolean existsByName(String name);
}
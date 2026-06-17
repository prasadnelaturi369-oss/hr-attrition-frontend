package com.hrms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entity.EmployeeHistory;

@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

	Page<EmployeeHistory> findByEmployeeId(Long employeeId, Pageable pageable);

	Page<EmployeeHistory> findByAction(String action, Pageable pageable);
}
package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);

	Optional<Employee> findByEmployeeCode(String employeeCode);

	Page<Employee> findByStatus(String status, Pageable pageable);

	Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

	Page<Employee> findByRole(String role, Pageable pageable);

	@Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:name% OR e.lastName LIKE %:name%")
	Page<Employee> searchByName(@Param("name") String name, Pageable pageable);

	boolean existsByEmail(String email);

	boolean existsByEmployeeCode(String employeeCode);

	long countByStatus(String status);

	@Query("SELECT e FROM Employee e WHERE e.department.id = :deptId AND e.status = 'ACTIVE'")
	List<Employee> findActiveEmployeesByDepartment(@Param("deptId") Long deptId);
}
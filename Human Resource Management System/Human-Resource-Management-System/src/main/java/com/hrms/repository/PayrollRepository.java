package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

	Optional<Payroll> findByEmployeeIdAndMonth(Long employeeId, String month);

	Page<Payroll> findByEmployeeId(Long employeeId, Pageable pageable);

	Page<Payroll> findByMonth(String month, Pageable pageable);

	Page<Payroll> findByStatus(String status, Pageable pageable);

	@Query("SELECT p FROM Payroll p WHERE p.month = :month AND p.status = 'GENERATED'")
	List<Payroll> findUnprocessedPayrollsForMonth(@Param("month") String month);

	@Query("SELECT AVG(p.netSalary) FROM Payroll p WHERE p.month = :month")
	Double getAverageSalaryForMonth(@Param("month") String month);

	@Query("SELECT SUM(p.netSalary) FROM Payroll p WHERE p.month = :month")
	Double getTotalSalaryPayoutForMonth(@Param("month") String month);

	boolean existsByEmployeeIdAndMonth(Long employeeId, String month);
}
package com.hrms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hrms.entity.LeaveRequest;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

	Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

	Page<LeaveRequest> findByStatus(String status, Pageable pageable);

	Page<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, String status, Pageable pageable);

	@Query("SELECT l FROM LeaveRequest l WHERE l.employee.id = :employeeId AND l.status = 'PENDING'")
	List<LeaveRequest> findPendingLeavesByEmployee(@Param("employeeId") Long employeeId);

	@Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM LeaveRequest l "
			+ "WHERE l.employee.id = :employeeId AND l.status = 'APPROVED' "
			+ "AND l.startDate <= :endDate AND l.endDate >= :startDate")
	boolean existsOverlappingLeave(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT l FROM LeaveRequest l WHERE l.approvedBy.id = :approverId")
	Page<LeaveRequest> findByApproverId(@Param("approverId") Long approverId, Pageable pageable);

	long countByEmployeeIdAndStatus(Long employeeId, String status);
}
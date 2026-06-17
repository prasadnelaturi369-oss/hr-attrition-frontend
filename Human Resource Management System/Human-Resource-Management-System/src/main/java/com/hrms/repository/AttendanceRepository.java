package com.hrms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate date);

	Page<Attendance> findByEmployeeId(Long employeeId, Pageable pageable);

	Page<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId AND a.attendanceDate BETWEEN :startDate AND :endDate")
	List<Attendance> getAttendanceForPeriod(@Param("employeeId") Long employeeId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId AND a.attendanceDate BETWEEN :startDate AND :endDate AND a.status = 'PRESENT'")
	long countPresentDays(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId AND a.attendanceDate BETWEEN :startDate AND :endDate AND a.status = 'ABSENT'")
	long countAbsentDays(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId AND a.attendanceDate BETWEEN :startDate AND :endDate AND a.status = 'LATE'")
	long countLateDays(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	boolean existsByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate date);
}
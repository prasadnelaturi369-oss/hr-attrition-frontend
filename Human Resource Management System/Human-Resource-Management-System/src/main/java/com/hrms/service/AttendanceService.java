package com.hrms.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import com.hrms.exception.BusinessException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.payload.request.AttendanceRequest;
import com.hrms.payload.response.AttendanceResponse;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

	private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

	private final AttendanceRepository attendanceRepository;
	private final EmployeeRepository employeeRepository;

	public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
		this.attendanceRepository = attendanceRepository;
		this.employeeRepository = employeeRepository;
	}

	@Transactional
	public AttendanceResponse markAttendance(AttendanceRequest request) {
		log.info("Marking attendance for employee: {} on date: {}", request.getEmployeeId(),
				request.getAttendanceDate());

		Employee employee = employeeRepository.findById(request.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		if (attendanceRepository.existsByEmployeeIdAndAttendanceDate(request.getEmployeeId(),
				request.getAttendanceDate())) {
			throw new BusinessException("Attendance already marked for this date");
		}

		Attendance attendance = new Attendance();
		attendance.setEmployee(employee);
		attendance.setAttendanceDate(request.getAttendanceDate());
		attendance.setStatus(request.getStatus());
		attendance.setRemarks(request.getRemarks());

		if ("PRESENT".equals(request.getStatus())) {
			attendance.setCheckInTime(java.time.LocalDateTime.now());
		}

		Attendance savedAttendance = attendanceRepository.save(attendance);
		log.info("Attendance marked successfully");

		return mapToResponse(savedAttendance);
	}

	@Transactional
	public AttendanceResponse updateCheckOut(Long attendanceId) {
		Attendance attendance = attendanceRepository.findById(attendanceId)
				.orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

		attendance.setCheckOutTime(java.time.LocalDateTime.now());
		if (attendance.getCheckInTime() != null) {
			double hours = (java.time.Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime())
					.toMinutes()) / 60.0;
			attendance.setWorkingHours(Math.round(hours * 100.0) / 100.0);
		}

		Attendance updatedAttendance = attendanceRepository.save(attendance);
		return mapToResponse(updatedAttendance);
	}

	public Page<AttendanceResponse> getAttendanceByEmployee(Long employeeId, Pageable pageable) {
		return attendanceRepository.findByEmployeeId(employeeId, pageable).map(this::mapToResponse);
	}

	public AttendanceSummary getAttendanceSummary(Long employeeId, LocalDate startDate, LocalDate endDate) {
		long present = attendanceRepository.countPresentDays(employeeId, startDate, endDate);
		long absent = attendanceRepository.countAbsentDays(employeeId, startDate, endDate);
		long late = attendanceRepository.countLateDays(employeeId, startDate, endDate);

		return new AttendanceSummary(present, absent, late);
	}

	private AttendanceResponse mapToResponse(Attendance attendance) {
		AttendanceResponse response = new AttendanceResponse();
		response.setId(attendance.getId());
		response.setEmployeeId(attendance.getEmployee().getId());
		response.setEmployeeName(attendance.getEmployee().getFullName());
		response.setAttendanceDate(attendance.getAttendanceDate());
		response.setStatus(attendance.getStatus());
		response.setCheckInTime(attendance.getCheckInTime());
		response.setCheckOutTime(attendance.getCheckOutTime());
		response.setWorkingHours(attendance.getWorkingHours());
		response.setRemarks(attendance.getRemarks());
		response.setCreatedAt(attendance.getCreatedAt());
		return response;
	}

	public record AttendanceSummary(long present, long absent, long late) {
	}
}
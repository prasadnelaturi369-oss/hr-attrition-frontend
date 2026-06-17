package com.hrms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.payload.request.AttendanceRequest;
import com.hrms.payload.response.AttendanceResponse;
import com.hrms.service.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attendance Management", description = "APIs for managing attendance")
public class AttendanceController {

	private final AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostMapping("/mark")
	@Operation(summary = "Mark attendance")
	public ResponseEntity<AttendanceResponse> markAttendance(@Valid @RequestBody AttendanceRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.markAttendance(request));
	}

	@PutMapping("/{attendanceId}/checkout")
	@Operation(summary = "Update check-out time")
	public ResponseEntity<AttendanceResponse> updateCheckOut(@PathVariable Long attendanceId) {
		return ResponseEntity.ok(attendanceService.updateCheckOut(attendanceId));
	}

	@GetMapping("/employee/{employeeId}")
	@Operation(summary = "Get attendance by employee")
	public ResponseEntity<Page<AttendanceResponse>> getAttendanceByEmployee(@PathVariable Long employeeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("attendanceDate").descending());
		return ResponseEntity.ok(attendanceService.getAttendanceByEmployee(employeeId, pageable));
	}

	@GetMapping("/summary/{employeeId}")
	@Operation(summary = "Get attendance summary for date range")
	public ResponseEntity<AttendanceService.AttendanceSummary> getAttendanceSummary(@PathVariable Long employeeId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

		return ResponseEntity.ok(attendanceService.getAttendanceSummary(employeeId, startDate, endDate));
	}
}
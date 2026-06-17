package com.hrms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Employee;
import com.hrms.entity.LeaveRequest;
import com.hrms.exception.BusinessException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.payload.request.LeaveRequestPayload;
import com.hrms.payload.response.LeaveResponse;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveService {

	private static final Logger log = LoggerFactory.getLogger(LeaveService.class);

	private final LeaveRepository leaveRepository;
	private final EmployeeRepository employeeRepository;

	public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
		this.leaveRepository = leaveRepository;
		this.employeeRepository = employeeRepository;
	}

	@Transactional
	public LeaveResponse applyLeave(LeaveRequestPayload request) {
		log.info("Applying leave for employee: {}", request.getEmployeeId());

		Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(
				() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

		if (leaveRepository.existsOverlappingLeave(request.getEmployeeId(), request.getStartDate(),
				request.getEndDate())) {
			throw new BusinessException("You already have a leave request for this period");
		}

		LeaveRequest leave = new LeaveRequest();
		leave.setEmployee(employee);
		leave.setLeaveType(request.getLeaveType());
		leave.setStartDate(request.getStartDate());
		leave.setEndDate(request.getEndDate());
		leave.setReason(request.getReason());
		leave.setStatus("PENDING");

		LeaveRequest savedLeave = leaveRepository.save(leave);
		log.info("Leave applied successfully with id: {}", savedLeave.getId());

		return mapToResponse(savedLeave);
	}

	@Transactional
	public LeaveResponse approveLeave(Long leaveId, Long approverId) {
		log.info("Approving leave: {}", leaveId);

		LeaveRequest leave = leaveRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + leaveId));

		if (!"PENDING".equals(leave.getStatus())) {
			throw new BusinessException("Only pending leaves can be approved");
		}

		Employee approver = employeeRepository.findById(approverId)
				.orElseThrow(() -> new ResourceNotFoundException("Approver not found with id: " + approverId));

		leave.setStatus("APPROVED");
		leave.setApprovedBy(approver);
		leave.setApprovedAt(java.time.LocalDateTime.now());

		LeaveRequest approvedLeave = leaveRepository.save(leave);
		return mapToResponse(approvedLeave);
	}

	@Transactional
	public LeaveResponse rejectLeave(Long leaveId, String rejectionReason) {
		log.info("Rejecting leave: {}", leaveId);

		LeaveRequest leave = leaveRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + leaveId));

		leave.setStatus("REJECTED");
		leave.setRejectionReason(rejectionReason);

		LeaveRequest rejectedLeave = leaveRepository.save(leave);
		return mapToResponse(rejectedLeave);
	}

	public Page<LeaveResponse> getLeavesByEmployee(Long employeeId, Pageable pageable) {
		return leaveRepository.findByEmployeeId(employeeId, pageable).map(this::mapToResponse);
	}

	public Page<LeaveResponse> getPendingLeaves(Pageable pageable) {
		return leaveRepository.findByStatus("PENDING", pageable).map(this::mapToResponse);
	}

	private LeaveResponse mapToResponse(LeaveRequest leave) {
		LeaveResponse response = new LeaveResponse();
		response.setId(leave.getId());
		response.setEmployeeId(leave.getEmployee().getId());
		response.setEmployeeName(leave.getEmployee().getFullName());
		response.setLeaveType(leave.getLeaveType());
		response.setStartDate(leave.getStartDate());
		response.setEndDate(leave.getEndDate());
		response.setNumberOfDays(leave.getNumberOfDays());
		response.setReason(leave.getReason());
		response.setStatus(leave.getStatus());
		response.setRequestedAt(leave.getRequestedAt());
		if (leave.getApprovedBy() != null) {
			response.setApprovedBy(leave.getApprovedBy().getFullName());
		}
		response.setRejectionReason(leave.getRejectionReason());
		response.setApprovedAt(leave.getApprovedAt());
		return response;
	}
}
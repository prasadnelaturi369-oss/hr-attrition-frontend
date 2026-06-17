package com.hrms.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Employee;
import com.hrms.entity.Payroll;
import com.hrms.exception.BusinessException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.payload.request.PayrollRequest;
import com.hrms.payload.response.PayrollResponse;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveRepository;
import com.hrms.repository.PayrollRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollService {

	private static final Logger log = LoggerFactory.getLogger(PayrollService.class);

	private final PayrollRepository payrollRepository;
	private final EmployeeRepository employeeRepository;
	private final AttendanceRepository attendanceRepository;
	private final LeaveRepository leaveRepository;

	public PayrollService(PayrollRepository payrollRepository, EmployeeRepository employeeRepository,
			AttendanceRepository attendanceRepository, LeaveRepository leaveRepository) {

		this.payrollRepository = payrollRepository;
		this.employeeRepository = employeeRepository;
		this.attendanceRepository = attendanceRepository;
		this.leaveRepository = leaveRepository;
	}

	@Transactional
	public PayrollResponse generatePayroll(PayrollRequest request) {
		log.info("Generating payroll for employee: {} for month: {}", request.getEmployeeId(), request.getMonth());

		if (payrollRepository.existsByEmployeeIdAndMonth(request.getEmployeeId(), request.getMonth())) {
			throw new BusinessException("Payroll already generated for this month");
		}

		Employee employee = employeeRepository.findById(request.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		// Calculate attendance for the month
		YearMonth yearMonth = YearMonth.parse(request.getMonth());
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		long presentDays = attendanceRepository.countPresentDays(request.getEmployeeId(), startDate, endDate);
		long absentDays = attendanceRepository.countAbsentDays(request.getEmployeeId(), startDate, endDate);
		long leavesTaken = leaveRepository.countByEmployeeIdAndStatus(request.getEmployeeId(), "APPROVED");

		// Calculate salary
		double perDaySalary = employee.getBasicSalary() / getWorkingDaysInMonth(yearMonth);
		double salaryAfterAbsent = employee.getBasicSalary() - (absentDays * perDaySalary);

		// Calculate allowances (40% of basic)
		double hra = request.getHra() != null ? request.getHra() : salaryAfterAbsent * 0.20;
		double da = request.getDa() != null ? request.getDa() : salaryAfterAbsent * 0.10;
		double ta = request.getTa() != null ? request.getTa() : salaryAfterAbsent * 0.05;
		double otherAllowances = request.getOtherAllowances() != null ? request.getOtherAllowances() : 0;

		double totalEarnings = salaryAfterAbsent + hra + da + ta + otherAllowances;

		// Calculate deductions
		double pfDeduction = request.getPfDeduction() != null ? request.getPfDeduction() : totalEarnings * 0.12;
		double professionalTax = request.getProfessionalTax() != null ? request.getProfessionalTax() : 200;
		double incomeTax = request.getIncomeTax() != null ? request.getIncomeTax()
				: totalEarnings > 50000 ? totalEarnings * 0.10 : 0;
		double otherDeductions = request.getOtherDeductions() != null ? request.getOtherDeductions() : 0;

		double totalDeductions = pfDeduction + professionalTax + incomeTax + otherDeductions;
		double netSalary = totalEarnings - totalDeductions;

		Payroll payroll = new Payroll();
		payroll.setEmployee(employee);
		payroll.setMonth(request.getMonth());
		payroll.setBasicSalary(employee.getBasicSalary());
		payroll.setHra(hra);
		payroll.setDa(da);
		payroll.setTa(ta);
		payroll.setOtherAllowances(otherAllowances);
		payroll.setTotalEarnings(totalEarnings);
		payroll.setPfDeduction(pfDeduction);
		payroll.setProfessionalTax(professionalTax);
		payroll.setIncomeTax(incomeTax);
		payroll.setOtherDeductions(otherDeductions);
		payroll.setTotalDeductions(totalDeductions);
		payroll.setNetSalary(netSalary);
		payroll.setTotalPresentDays((int) presentDays);
		payroll.setTotalAbsentDays((int) absentDays);
		payroll.setTotalLeavesTaken((int) leavesTaken);
		payroll.setStatus("GENERATED");

		Payroll savedPayroll = payrollRepository.save(payroll);
		log.info("Payroll generated successfully for month: {}", request.getMonth());

		return mapToResponse(savedPayroll);
	}

	private int getWorkingDaysInMonth(YearMonth yearMonth) {
		int workingDays = 0;
		for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
			LocalDate date = yearMonth.atDay(i);
			if (date.getDayOfWeek().getValue() <= 5) {
				workingDays++;
			}
		}
		return workingDays;
	}

	public Page<PayrollResponse> getPayrollByEmployee(Long employeeId, Pageable pageable) {
		return payrollRepository.findByEmployeeId(employeeId, pageable).map(this::mapToResponse);
	}

	public Page<PayrollResponse> getPayrollByMonth(String month, Pageable pageable) {
		return payrollRepository.findByMonth(month, pageable).map(this::mapToResponse);
	}

	@Transactional
	public PayrollResponse processPayroll(Long payrollId) {
		Payroll payroll = payrollRepository.findById(payrollId)
				.orElseThrow(() -> new ResourceNotFoundException("Payroll not found"));

		payroll.setStatus("PROCESSED");
		Payroll processedPayroll = payrollRepository.save(payroll);
		return mapToResponse(processedPayroll);
	}

	private PayrollResponse mapToResponse(Payroll payroll) {
		PayrollResponse response = new PayrollResponse();
		response.setId(payroll.getId());
		response.setEmployeeId(payroll.getEmployee().getId());
		response.setEmployeeName(payroll.getEmployee().getFullName());
		response.setEmployeeCode(payroll.getEmployee().getEmployeeCode());
		response.setMonth(payroll.getMonth());
		response.setBasicSalary(payroll.getBasicSalary());
		response.setHra(payroll.getHra());
		response.setDa(payroll.getDa());
		response.setTa(payroll.getTa());
		response.setOtherAllowances(payroll.getOtherAllowances());
		response.setTotalEarnings(payroll.getTotalEarnings());
		response.setPfDeduction(payroll.getPfDeduction());
		response.setProfessionalTax(payroll.getProfessionalTax());
		response.setIncomeTax(payroll.getIncomeTax());
		response.setOtherDeductions(payroll.getOtherDeductions());
		response.setTotalDeductions(payroll.getTotalDeductions());
		response.setNetSalary(payroll.getNetSalary());
		response.setTotalPresentDays(payroll.getTotalPresentDays());
		response.setTotalAbsentDays(payroll.getTotalAbsentDays());
		response.setTotalLeavesTaken(payroll.getTotalLeavesTaken());
		response.setStatus(payroll.getStatus());
		response.setGeneratedAt(payroll.getGeneratedAt());
		return response;
	}
}
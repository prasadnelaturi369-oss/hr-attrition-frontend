package com.hrms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Department;
import com.hrms.entity.Employee;
import com.hrms.entity.EmployeeHistory;
import com.hrms.exception.BusinessException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.payload.request.EmployeeRequest;
import com.hrms.payload.response.EmployeeResponse;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.EmployeeHistoryRepository;
import com.hrms.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

	private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final EmployeeHistoryRepository historyRepository;

	public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
			EmployeeHistoryRepository historyRepository) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.historyRepository = historyRepository;
	}

	@Transactional
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		log.info("Creating new employee: {}", request.getEmail());

		if (employeeRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException("Employee with email " + request.getEmail() + " already exists");
		}

		Employee employee = new Employee();
		employee.setFirstName(request.getFirstName());
		employee.setLastName(request.getLastName());
		employee.setEmail(request.getEmail());
		employee.setPhone(request.getPhone());
		employee.setAddress(request.getAddress());
		employee.setJoiningDate(request.getJoiningDate());
		employee.setBasicSalary(request.getBasicSalary());
		employee.setDesignation(request.getDesignation());
		employee.setRole(request.getRole() != null ? request.getRole() : "EMPLOYEE");
		employee.setEmployeeCode(generateEmployeeCode());

		if (request.getDepartmentId() != null) {
			Department department = departmentRepository.findById(request.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
			employee.setDepartment(department);
		}

		Employee savedEmployee = employeeRepository.save(employee);
		logHistory(savedEmployee, "CREATED", null, null, "Employee onboarded");

		log.info("Employee created successfully with code: {}", savedEmployee.getEmployeeCode());
		return mapToResponse(savedEmployee);
	}

	public EmployeeResponse getEmployeeById(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
		return mapToResponse(employee);
	}

	public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable).map(this::mapToResponse);
	}

	@Transactional
	public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
		log.info("Updating employee with id: {}", id);

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		String oldDept = employee.getDepartment() != null ? employee.getDepartment().getName() : null;

		employee.setFirstName(request.getFirstName());
		employee.setLastName(request.getLastName());
		employee.setPhone(request.getPhone());
		employee.setAddress(request.getAddress());
		employee.setDesignation(request.getDesignation());

		if (request.getBasicSalary() != null && !request.getBasicSalary().equals(employee.getBasicSalary())) {
			logHistory(employee, "SALARY_CHANGED", String.valueOf(employee.getBasicSalary()),
					String.valueOf(request.getBasicSalary()), "Salary updated");
			employee.setBasicSalary(request.getBasicSalary());
		}

		if (request.getDepartmentId() != null) {
			Department department = departmentRepository.findById(request.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
			String newDept = department.getName();
			if (oldDept != null && !oldDept.equals(newDept)) {
				logHistory(employee, "DEPARTMENT_CHANGED", oldDept, newDept, "Department changed");
			}
			employee.setDepartment(department);
		}

		Employee updatedEmployee = employeeRepository.save(employee);
		return mapToResponse(updatedEmployee);
	}

	@Transactional
	public void terminateEmployee(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		employee.setStatus("TERMINATED");
		employeeRepository.save(employee);
		logHistory(employee, "TERMINATED", null, null, "Employee terminated");
		log.info("Employee terminated with id: {}", id);
	}

	public Page<EmployeeResponse> searchEmployees(String name, Pageable pageable) {
		return employeeRepository.searchByName(name, pageable).map(this::mapToResponse);
	}

	public Page<EmployeeResponse> getEmployeesByDepartment(Long departmentId, Pageable pageable) {
		return employeeRepository.findByDepartmentId(departmentId, pageable).map(this::mapToResponse);
	}

	private String generateEmployeeCode() {
		long count = employeeRepository.count() + 1;
		return "EMP" + String.format("%06d", count);
	}

	private void logHistory(Employee employee, String action, String oldValue, String newValue, String remarks) {
		EmployeeHistory history = new EmployeeHistory();
		history.setEmployee(employee);
		history.setAction(action);
		history.setOldValue(oldValue);
		history.setNewValue(newValue);
		history.setRemarks(remarks);
		historyRepository.save(history);
	}

	private EmployeeResponse mapToResponse(Employee employee) {
		EmployeeResponse response = new EmployeeResponse();
		response.setId(employee.getId());
		response.setFirstName(employee.getFirstName());
		response.setLastName(employee.getLastName());
		response.setFullName(employee.getFullName());
		response.setEmail(employee.getEmail());
		response.setEmployeeCode(employee.getEmployeeCode());
		response.setPhone(employee.getPhone());
		response.setAddress(employee.getAddress());
		response.setJoiningDate(employee.getJoiningDate());
		response.setBasicSalary(employee.getBasicSalary());
		response.setDesignation(employee.getDesignation());
		response.setRole(employee.getRole());
		response.setStatus(employee.getStatus());
		response.setCreatedAt(employee.getCreatedAt());
		response.setUpdatedAt(employee.getUpdatedAt());

		if (employee.getDepartment() != null) {
			response.setDepartmentId(employee.getDepartment().getId());
			response.setDepartmentName(employee.getDepartment().getName());
		}

		return response;
	}
}
package com.hrms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.payload.request.EmployeeRequest;
import com.hrms.payload.response.EmployeeResponse;
import com.hrms.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	@Operation(summary = "Create/Onboard employee")
	public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
		log.info("REST request to create employee");
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get employee by ID")
	public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
	}

	@GetMapping
	@Operation(summary = "Get all employees with pagination")
	public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
		return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update employee")
	public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequest request) {
		return ResponseEntity.ok(employeeService.updateEmployee(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Terminate employee")
	public ResponseEntity<Void> terminateEmployee(@PathVariable Long id) {
		employeeService.terminateEmployee(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	@Operation(summary = "Search employees by name")
	public ResponseEntity<Page<EmployeeResponse>> searchEmployees(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(employeeService.searchEmployees(name, pageable));
	}

	@GetMapping("/department/{departmentId}")
	@Operation(summary = "Get employees by department")
	public ResponseEntity<Page<EmployeeResponse>> getEmployeesByDepartment(@PathVariable Long departmentId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId, pageable));
	}
}
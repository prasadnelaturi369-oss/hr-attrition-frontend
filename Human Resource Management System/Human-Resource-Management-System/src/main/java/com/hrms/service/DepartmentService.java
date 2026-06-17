package com.hrms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Department;
import com.hrms.exception.BusinessException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.payload.request.DepartmentRequest;
import com.hrms.payload.response.DepartmentResponse;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

	private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

	private final DepartmentRepository departmentRepository;
	private final EmployeeRepository employeeRepository;

	public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
		this.departmentRepository = departmentRepository;
		this.employeeRepository = employeeRepository;
	}

	@Transactional
	public DepartmentResponse createDepartment(DepartmentRequest request) {
		log.info("Creating new department: {}", request.getName());

		if (departmentRepository.existsByName(request.getName())) {
			throw new BusinessException("Department with name " + request.getName() + " already exists");
		}

		Department department = new Department();
		department.setName(request.getName());
		department.setDescription(request.getDescription());

		Department savedDepartment = departmentRepository.save(department);
		log.info("Department created successfully with id: {}", savedDepartment.getId());

		return mapToResponse(savedDepartment);
	}

	public DepartmentResponse getDepartmentById(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
		return mapToResponse(department);
	}

	public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
		return departmentRepository.findAll(pageable).map(this::mapToResponse);
	}

	@Transactional
	public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
		log.info("Updating department with id: {}", id);

		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

		if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
			throw new BusinessException("Department with name " + request.getName() + " already exists");
		}

		department.setName(request.getName());
		department.setDescription(request.getDescription());

		Department updatedDepartment = departmentRepository.save(department);
		return mapToResponse(updatedDepartment);
	}

	@Transactional
	public void deleteDepartment(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

		long employeeCount = employeeRepository.countByStatus("ACTIVE");
		if (employeeCount > 0) {
			throw new BusinessException("Cannot delete department with active employees");
		}

		departmentRepository.delete(department);
		log.info("Department deleted with id: {}", id);
	}

	private DepartmentResponse mapToResponse(Department department) {
		DepartmentResponse response = new DepartmentResponse();
		response.setId(department.getId());
		response.setName(department.getName());
		response.setDescription(department.getDescription());
		response.setCreatedAt(department.getCreatedAt());
		response.setEmployeeCount(employeeRepository.countByStatus("ACTIVE"));
		return response;
	}
}
package com.banking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.entity.Customer;
import com.banking.exception.BankingException;
import com.banking.payload.request.CustomerRequest;
import com.banking.payload.response.CustomerResponse;
import com.banking.repository.CustomerRepository;

@Service
public class CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
	private final CustomerRepository customerRepository;
	private final AuditService auditService;

	public CustomerService(CustomerRepository customerRepository, AuditService auditService) {
		this.customerRepository = customerRepository;
		this.auditService = auditService;
	}

	@Transactional
	public CustomerResponse createCustomer(CustomerRequest request) {
		log.info("Creating customer with email: {}", request.getEmail());

		if (customerRepository.existsByEmail(request.getEmail())) {
			throw new BankingException("Customer with email " + request.getEmail() + " already exists");
		}

		if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new BankingException("Customer with phone number " + request.getPhoneNumber() + " already exists");
		}

		Customer customer = new Customer();
		customer.setFirstName(request.getFirstName());
		customer.setLastName(request.getLastName());
		customer.setEmail(request.getEmail());
		customer.setPhoneNumber(request.getPhoneNumber());
		customer.setAddress(request.getAddress());

		Customer saved = customerRepository.save(customer);

		auditService.log("CREATE_CUSTOMER", "Customer", String.valueOf(saved.getId()),
				"Customer created: " + saved.getEmail());

		CustomerResponse response = new CustomerResponse();
		response.setId(saved.getId());
		response.setFirstName(saved.getFirstName());
		response.setLastName(saved.getLastName());
		response.setFullName(saved.getFirstName() + " " + saved.getLastName());
		response.setEmail(saved.getEmail());
		response.setPhoneNumber(saved.getPhoneNumber());
		response.setAddress(saved.getAddress());
		response.setCreatedAt(saved.getCreatedAt());

		return CustomerResponse.success(response, "Customer created successfully");
	}

	public CustomerResponse getCustomer(Long id) {
		log.info("Fetching customer with ID: {}", id);

		Customer customer = customerRepository.findById(id)
			    .orElseThrow(() -> new BankingException("Customer not found with ID: " + id));

		CustomerResponse response = new CustomerResponse();
		response.setId(customer.getId());
		response.setFirstName(customer.getFirstName());
		response.setLastName(customer.getLastName());
		response.setFullName(customer.getFirstName() + " " + customer.getLastName());
		response.setEmail(customer.getEmail());
		response.setPhoneNumber(customer.getPhoneNumber());
		response.setAddress(customer.getAddress());
		response.setCreatedAt(customer.getCreatedAt());

		return CustomerResponse.success(response, "Customer found");
	}
}
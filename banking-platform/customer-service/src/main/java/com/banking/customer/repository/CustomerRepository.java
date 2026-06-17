package com.banking.customer.repository;

import com.banking.customer.entity.Customer;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {
    
    private final Map<String, Customer> customerStore = new ConcurrentHashMap<>();
    private final Map<String, Customer> emailIndex = new ConcurrentHashMap<>();
    
    public Customer save(Customer customer) {
        customerStore.put(customer.getId(), customer);
        emailIndex.put(customer.getEmail(), customer);
        return customer;
    }
    
    public Optional<Customer> findByEmail(String email) {
        return Optional.ofNullable(emailIndex.get(email));
    }
    
    public boolean existsByEmail(String email) {
        return emailIndex.containsKey(email);
    }
    
    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customerStore.get(id));
    }
}
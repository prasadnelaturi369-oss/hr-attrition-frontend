package com.banking.customer.controller;

import com.banking.customer.payload.request.LoginRequest;
import com.banking.customer.payload.request.RegisterRequest;
import com.banking.customer.payload.response.AuthResponse;
import com.banking.customer.entity.Customer;
import com.banking.customer.service.CustomerService;
import com.banking.customer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        Customer customer = customerService.register(request);
        String token = jwtUtil.generateToken(customer.getId(), customer.getEmail(), customer.getRole());
        return new AuthResponse(token, "Bearer", customer.getId(), customer.getEmail(), customer.getRole());
    }
    
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Customer customer = customerService.login(request);
        String token = jwtUtil.generateToken(customer.getId(), customer.getEmail(), customer.getRole());
        return new AuthResponse(token, "Bearer", customer.getId(), customer.getEmail(), customer.getRole());
    }
}
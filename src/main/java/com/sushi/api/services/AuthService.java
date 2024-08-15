package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Customer;
import com.sushi.api.model.Employee;
import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.LoginResponseDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;
import com.sushi.api.model.dto.login.RegisterResponseDTO;
import com.sushi.api.repositories.CustomerRepository;
import com.sushi.api.repositories.EmployeeRepository;
import com.sushi.api.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public AuthService(TokenService tokenService, PasswordEncoder passwordEncoder, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    public LoginResponseDTO loginCustomer(LoginRequestDTO dto) {
        Customer customer = customerRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        if (passwordEncoder.matches(dto.password(), customer.getPassword())) {
            String token = tokenService.generateCustomerToken(customer);
            Instant expiresAt = tokenService.getExpirationDateFromToken(token);
            return new LoginResponseDTO(customer.getName(), token, expiresAt.toString());
        }

        return new LoginResponseDTO("Invalid credentials", null, null);
    }

    @Transactional
    public RegisterResponseDTO registerCustomer(RegisterRequestDTO dto) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(dto.email());
        if (existingCustomer.isEmpty()) {
            Customer newCustomer = new Customer();
            newCustomer.setPassword(passwordEncoder.encode(dto.password()));
            newCustomer.setEmail(dto.email());
            newCustomer.setName(dto.name());

            customerRepository.save(newCustomer);
            String token = tokenService.generateCustomerToken(newCustomer);
            Instant expiresAt = tokenService.getExpirationDateFromToken(token);
            return new RegisterResponseDTO(newCustomer.getName(), token, expiresAt.toString());
        }

        return new RegisterResponseDTO("Customer already exists", null, null);
    }

    public LoginResponseDTO loginEmployee(LoginRequestDTO dto) {
        Employee employee = employeeRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        if (passwordEncoder.matches(dto.password(), employee.getPassword())) {
            String token = tokenService.generateEmployeeToken(employee);
            Instant expiresAt = tokenService.getExpirationDateFromToken(token);
            return new LoginResponseDTO(employee.getName(), token, expiresAt.toString());
        }

        return new LoginResponseDTO("Invalid credentials", null, null);
    }

    @Transactional
    public RegisterResponseDTO registerEmployee(RegisterRequestDTO dto) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(dto.email());
        if (existingEmployee.isEmpty()) {
            Employee newEmployee = new Employee();
            newEmployee.setPassword(passwordEncoder.encode(dto.password()));
            newEmployee.setEmail(dto.email());
            newEmployee.setName(dto.name());
            employeeRepository.save(newEmployee);
            String token = tokenService.generateEmployeeToken(newEmployee);
            Instant expiresAt = tokenService.getExpirationDateFromToken(token);
            return new RegisterResponseDTO(newEmployee.getName(), token, expiresAt.toString());
        }

        return new RegisterResponseDTO("Employee already exists", null, null);
    }
}

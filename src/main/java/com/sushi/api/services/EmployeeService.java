package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Employee;
import com.sushi.api.model.dto.employee.EmployeeRequestDTO;
import com.sushi.api.model.dto.employee.EmployeeUpdateDTO;
import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.LoginResponseDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;
import com.sushi.api.repositories.EmployeeRepository;
import com.sushi.api.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    public Page<Employee> listAllPageable(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public List<Employee> listAllNonPageable() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with this id."));
    }

    public Employee findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with this id."));
    }

    public LoginResponseDTO loginEmployee(LoginRequestDTO dto) {
        Employee employee = employeeRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        if (passwordEncoder.matches(dto.password(), employee.getPassword())) {
            String token = tokenService.generateEmployeeToken(employee);
            return new LoginResponseDTO(employee.getName(), token);
        }

        return new LoginResponseDTO("Invalid credentials", null);
    }

    @Transactional
    public LoginResponseDTO registerEmployee(RegisterRequestDTO dto) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(dto.email());
        if (existingEmployee.isEmpty()) {
            Employee newEmployee = new Employee();
            newEmployee.setPassword(passwordEncoder.encode(dto.password()));
            newEmployee.setEmail(dto.email());
            newEmployee.setName(dto.name());
            employeeRepository.save(newEmployee);
            String token = tokenService.generateEmployeeToken(newEmployee);
            return new LoginResponseDTO(newEmployee.getName(), token);
        }

        return new LoginResponseDTO("Employee already exists", null);
    }

    @Transactional
    public Employee createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepository.findByEmail(dto.email()).isPresent()) {
            throw new DataIntegrityViolationException("Data integrity violation error occurred.");
        }

        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setPassword(dto.password());

        return employeeRepository.save(employee);
    }

    @Transactional
    public void replaceEmployee(EmployeeUpdateDTO dto) {
        Employee savedEmployee = findEmployeeById(dto.id());
        savedEmployee.setName(dto.name());
        savedEmployee.setEmail(dto.email());
        savedEmployee.setPassword(dto.password());
        employeeRepository.save(savedEmployee);
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        employeeRepository.delete(findEmployeeById(id));
    }
}
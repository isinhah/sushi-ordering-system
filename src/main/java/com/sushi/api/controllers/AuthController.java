package com.sushi.api.controllers;

import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.LoginResponseDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;
import com.sushi.api.services.CustomerService;
import com.sushi.api.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public AuthController(CustomerService customerService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @PostMapping("/customers/login")
    public ResponseEntity<LoginResponseDTO> loginCustomer(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = customerService.loginCustomer(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customers/register")
    public ResponseEntity<LoginResponseDTO> registerCustomer(@RequestBody RegisterRequestDTO dto) {
        LoginResponseDTO response = customerService.registerCustomer(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employees/login")
    public ResponseEntity<LoginResponseDTO> loginEmployee(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = employeeService.loginEmployee(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employees/register")
    public ResponseEntity<LoginResponseDTO> registerEmployee(@RequestBody RegisterRequestDTO dto) {
        LoginResponseDTO response = employeeService.registerEmployee(dto);
        return ResponseEntity.ok(response);
    }
}

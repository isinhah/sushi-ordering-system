package com.sushi.api.controllers;

import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.LoginResponseDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;
import com.sushi.api.model.dto.login.RegisterResponseDTO;
import com.sushi.api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = {"application/json"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticate customer",
            description = "Verifies customer credentials and returns an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials, authentication failed"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/customers/login")
    public ResponseEntity<LoginResponseDTO> loginCustomer(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.loginCustomer(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Register a new customer",
            description = "Registers a new customer and returns an authentication token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "400", description = "Customer already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/customers/register")
    public ResponseEntity<RegisterResponseDTO> registerCustomer(@RequestBody RegisterRequestDTO dto) {
        RegisterResponseDTO response = authService.registerCustomer(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Authenticate employee",
            description = "Verifies employee credentials and returns an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials, authentication failed"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/employees/login")
    public ResponseEntity<LoginResponseDTO> loginEmployee(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.loginEmployee(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Register a new employee",
            description = "Registers a new employee and returns an authentication token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Employee already exists with the provided email"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/employees/register")
    public ResponseEntity<RegisterResponseDTO> registerEmployee(@RequestBody RegisterRequestDTO dto) {
        RegisterResponseDTO response = authService.registerEmployee(dto);
        return ResponseEntity.ok(response);
    }
}

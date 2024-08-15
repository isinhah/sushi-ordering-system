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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.sushi.api.common.CustomerConstants.*;
import static com.sushi.api.common.EmployeeConstants.EMPLOYEE_LOGIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Should return a LoginResponseDTO with a token when credentials are valid")
    void loginCustomer_WithValidCredentials_ReturnsToken() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);
        Instant expirationDate = Instant.now().plus(1, ChronoUnit.HOURS);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER_LOGIN));
        when(passwordEncoder.matches(PASSWORD, CUSTOMER_LOGIN.getPassword())).thenReturn(true);
        when(tokenService.generateCustomerToken(CUSTOMER_LOGIN)).thenReturn(TOKEN);
        when(tokenService.getExpirationDateFromToken(TOKEN)).thenReturn(expirationDate);

        LoginResponseDTO response = authService.loginCustomer(request);

        assertNotNull(response);
        assertEquals(CUSTOMER_LOGIN.getName(), response.name());
        assertEquals(TOKEN, response.token());
        assertEquals(expirationDate.toString(), response.expiresAt());
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when password is invalid")
    void loginCustomer_WithInvalidPassword_ReturnsInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, "senhaerrada");

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER_LOGIN));
        when(passwordEncoder.matches("senhaerrada", CUSTOMER_LOGIN.getPassword())).thenReturn(false);

        LoginResponseDTO response = authService.loginCustomer(request);

        assertNotNull(response);
        assertEquals("Invalid credentials", response.name());
        assertNull(response.token());
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when customer does not exist")
    void loginCustomer_WithNonExistingCustomer_ThrowsResourceNotFoundException() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.loginCustomer(request));
    }

    @Test
    @DisplayName("Should register a new customer and return a RegisterResponseDTO with a valid token")
    void registerCustomer_SavesNewCustomer_ReturnsToken() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);
        Instant expirationDate = Instant.now().plus(1, ChronoUnit.HOURS);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(tokenService.generateCustomerToken(any(Customer.class))).thenReturn(TOKEN);
        when(tokenService.getExpirationDateFromToken(TOKEN)).thenReturn(expirationDate);

        RegisterResponseDTO response = authService.registerCustomer(request);

        assertNotNull(response);
        assertEquals("ana", response.name());
        assertEquals(TOKEN, response.token());
        assertEquals(expirationDate.toString(), response.expiresAt());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should return a RegisterResponseDTO with invalid credentials message when customer already exists")
    void registerCustomer_WithExistingCustomer_ReturnsInvalidCredentials() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new Customer()));

        RegisterResponseDTO response = authService.registerCustomer(request);

        assertNotNull(response);
        assertEquals("Customer already exists", response.name());
        assertNull(response.token());

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with a token when credentials are valid")
    void loginEmployee_WithValidCredentials_ReturnsToken() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);
        Instant expirationDate = Instant.now().plus(1, ChronoUnit.HOURS);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.of(EMPLOYEE_LOGIN));
        when(passwordEncoder.matches(PASSWORD, EMPLOYEE_LOGIN.getPassword())).thenReturn(true);
        when(tokenService.generateEmployeeToken(EMPLOYEE_LOGIN)).thenReturn(TOKEN);
        when(tokenService.getExpirationDateFromToken(TOKEN)).thenReturn(expirationDate);

        LoginResponseDTO response = authService.loginEmployee(request);

        assertNotNull(response);
        assertEquals(EMPLOYEE_LOGIN.getName(), response.name());
        assertEquals(TOKEN, response.token());
        assertEquals(expirationDate.toString(), response.expiresAt());
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when password is invalid")
    void loginEmployee_WithInvalidPassword_ReturnsInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, "senhaerrada");

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.of(EMPLOYEE_LOGIN));
        when(passwordEncoder.matches("senhaerrada", EMPLOYEE_LOGIN.getPassword())).thenReturn(false);

        LoginResponseDTO response = authService.loginEmployee(request);

        assertNotNull(response);
        assertEquals("Invalid credentials", response.name());
        assertNull(response.token());
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when employee does not exist")
    void loginEmployee_WithNonExistingEmployee_ThrowsResourceNotFoundException() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.loginEmployee(request));
    }

    @Test
    @DisplayName("Should register a new employee and return a LoginRequestDTO with a valid token")
    void registerEmployee_SavesNewEmployee_ReturnsToken() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);
        Instant expirationDate = Instant.now().plus(1, ChronoUnit.HOURS);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(tokenService.generateEmployeeToken(any(Employee.class))).thenReturn(TOKEN);
        when(tokenService.getExpirationDateFromToken(TOKEN)).thenReturn(expirationDate);

        RegisterResponseDTO response = authService.registerEmployee(request);

        assertNotNull(response);
        assertEquals("ana", response.name());
        assertEquals(TOKEN, response.token());
        assertEquals(expirationDate.toString(), response.expiresAt());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when employee already exists")
    void registerCostumer_WithExistingEmployee_ReturnsInvalidCredentials() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new Employee()));

        RegisterResponseDTO response = authService.registerEmployee(request);

        assertNotNull(response);
        assertEquals("Employee already exists", response.name());
        assertNull(response.token());

        verify(employeeRepository, never()).save(any(Employee.class));
    }
}

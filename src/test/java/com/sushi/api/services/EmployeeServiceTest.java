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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sushi.api.common.CustomerConstants.*;
import static com.sushi.api.common.EmployeeConstants.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;

    @Test
    @DisplayName("Should return a list of employees inside page object when successful")
    void listAll_ReturnsListOfEmployeesInsidePageObject_WhenSuccessful() {
        Page<Employee> employeePage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
        Page<Employee> result = employeeService.listAllPageable(pageable);

        assertNotNull(result);
        assertEquals(employeePage, result);
    }

    @Test
    @DisplayName("Should return an empty list of employees inside page object when there are no employees")
    void listAllPageable_ReturnsEmptyListOfEmployeesInsidePageObject_WhenThereAreNoEmployees() {
        Page<Employee> emptyEmployeePage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = mock(Pageable.class);

        when(employeeRepository.findAll(pageable)).thenReturn(emptyEmployeePage);

        Page<Employee> result = employeeService.listAllPageable(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of employees when successful")
    void listAllNonPageable_ReturnsListOfEmployees_WhenSuccessful() {
        when(employeeRepository.findAll()).thenReturn(EMPLOYEES);

        List<Employee> result = employeeService.listAllNonPageable();

        assertNotNull(result);
        assertEquals(EMPLOYEES.size(), result.size());
    }

    @Test
    @DisplayName("Should return an empty list of employees when there are no employees")
    void listAllNonPageable_ReturnsEmptyListOfEmployees_WhenThereAreNoEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = employeeService.listAllNonPageable();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return an employee by id when successful")
    void findEmployeeById_ReturnsEmployee_WhenSuccessful() {
        when(employeeRepository.findById(EMPLOYEE.getId())).thenReturn(Optional.of(EMPLOYEE));

        Employee result = employeeService.findEmployeeById(EMPLOYEE.getId());

        assertNotNull(result);
        assertEquals(EMPLOYEE, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when employee id does not exist")
    void findEmployeeById_ThrowsResourceNotFoundException_WhenEmployeeIdDoesNotExist() {
        when(employeeRepository.findById(EMPLOYEE.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.findEmployeeById(EMPLOYEE.getId()));
    }

    @Test
    @DisplayName("Should return an employee by email when successful")
    void findEmployeeByEmail_ReturnsEmployee_WhenSuccessful() {
        String email = "isabel@gmail.com";

        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(EMPLOYEE));

        Employee result = employeeService.findEmployeeByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when employee email does not exist")
    void findEmployeeByEmail_ThrowsResourceNotFoundException_WhenEmployeeEmailDoesNotExist() {
        String email = "joao@gmail.com";

        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(EMPLOYEE2));

        Employee result = employeeService.findEmployeeByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with a token when credentials are valid")
    void loginEmployee_WithValidCredentials_ReturnsToken() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.of(EMPLOYEE_LOGIN));
        when(passwordEncoder.matches(PASSWORD, EMPLOYEE_LOGIN.getPassword())).thenReturn(true);
        when(tokenService.generateEmployeeToken(EMPLOYEE_LOGIN)).thenReturn(TOKEN);

        LoginResponseDTO response = employeeService.loginEmployee(request);

        assertNotNull(response);
        assertEquals(EMPLOYEE_LOGIN.getName(), response.name());
        assertEquals(TOKEN, response.token());
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when password is invalid")
    void loginEmployee_WithInvalidPassword_ReturnsInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, "senhaerrada");

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.of(EMPLOYEE_LOGIN));
        when(passwordEncoder.matches("senhaerrada", EMPLOYEE_LOGIN.getPassword())).thenReturn(false);

        LoginResponseDTO response = employeeService.loginEmployee(request);

        assertNotNull(response);
        assertEquals("Invalid credentials", response.name());
        assertNull(response.token());
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when employee does not exist")
    void loginEmployee_WithNonExistingEmployee_ThrowsResourceNotFoundException() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.loginEmployee(request));
    }

    @Test
    @DisplayName("Should register a new employee and return a LoginRequestDTO with a valid token")
    void registerEmployee_SavesNewEmployee_ReturnsToken() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(tokenService.generateEmployeeToken(any(Employee.class))).thenReturn(TOKEN);

        LoginResponseDTO response = employeeService.registerEmployee(request);

        assertNotNull(response);
        assertEquals("ana", response.name());
        assertEquals(TOKEN, response.token());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when employee already exists")
    void registerCostumer_WithExistingEmployee_ReturnsInvalidCredentials() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);

        when(employeeRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new Employee()));

        LoginResponseDTO response = employeeService.registerEmployee(request);

        assertNotNull(response);
        assertEquals("Employee already exists", response.name());
        assertNull(response.token());

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should create a new employee when provided with valid EmployeeRequestDTO")
    void createEmployee_WithValidData_CreatesEmployee() {
        EmployeeRequestDTO request = new EmployeeRequestDTO(EMPLOYEE.getName(), EMPLOYEE.getEmail(), EMPLOYEE.getPassword());

        when(employeeRepository.save(any(Employee.class))).thenReturn(EMPLOYEE);

        Employee result = employeeService.createEmployee(request);

        assertNotNull(result);
        assertEquals(EMPLOYEE.getName(), result.getName());
        assertEquals(EMPLOYEE.getEmail(), result.getEmail());
        assertEquals(EMPLOYEE.getPassword(), result.getPassword());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw a DataIntegrityViolationException when email already exists")
    void createEmployee_WithExistingEmail_ThrowsDataIntegrityViolationException() {
        EmployeeRequestDTO request = new EmployeeRequestDTO(EMPLOYEE.getName(), EMPLOYEE.getEmail(), EMPLOYEE.getPassword());

        when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.of(EMPLOYEE));

        assertThrows(DataIntegrityViolationException.class, () -> employeeService.createEmployee(request));
    }

    @Test
    @DisplayName("Should replace an existing employee when provided with valid CustomerUpdateDTO")
    void replaceEmployee_WhenSuccessful() {
        when(employeeRepository.findById(EMPLOYEE.getId())).thenReturn(Optional.of(EMPLOYEE));

        EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO(
                EMPLOYEE.getId(),
                "newName",
                "newEmail",
                "newPassword"
        );

        employeeService.replaceEmployee(updateDTO);

        verify(employeeRepository).findById(EMPLOYEE.getId());
        verify(employeeRepository).save(EMPLOYEE);
    }

    @Test
    @DisplayName("Should delete an employee by id when successful")
    void deleteEmployee_WithExistingId_WhenSuccessful() {
        when(employeeRepository.findById(EMPLOYEE.getId())).thenReturn(Optional.of(EMPLOYEE));

        assertThatCode(() -> employeeService.deleteEmployee(EMPLOYEE.getId())).doesNotThrowAnyException();

        verify(employeeRepository, times(1)).delete(EMPLOYEE);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when employee id does not exist")
    void deleteEmployee_ThrowsResourceNotFoundException_WhenIdDoesNotExist() {
        when(employeeRepository.findById(EMPLOYEE.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(EMPLOYEE.getId()));
    }
}
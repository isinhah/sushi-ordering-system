package com.sushi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Employee;
import com.sushi.api.security.TokenService;
import com.sushi.api.services.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.sushi.api.common.EmployeeConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of employees inside page object when successful")
    public void listAllPageable_ReturnsAllEmployeesWithPagination() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(EMPLOYEES, pageable, EMPLOYEES.size());

        when(employeeService.listAllPageable(pageable)).thenReturn(page);

        String expectedJson = objectMapper.writeValueAsString(EMPLOYEES);

        mockMvc
                .perform(get("/api/employees")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of employees when successful")
    public void listAllNonPageable_ReturnsAllEmployees() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(EMPLOYEES);

        when(employeeService.listAllNonPageable()).thenReturn(EMPLOYEES);

        mockMvc.perform(get("/api/employees/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return an employee by id when successful")
    public void findEmployeeById_ReturnsEmployeeById() throws Exception {
        when(employeeService.findEmployeeById(EMPLOYEE.getId())).thenReturn(EMPLOYEE);

        String expectedJson = objectMapper.writeValueAsString(EMPLOYEE);

        mockMvc.perform(get("/api/employees/{id}", EMPLOYEE.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return ResourceNotFoundException when trying to find a employee by id that does not exist")
    public void findEmployeeById_ReturnsNotFound_WhenEmployeeDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(employeeService.findEmployeeById(nonExistentId)).thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/employees/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return an employee by email when successful")
    public void findEmployeeByEmail_ReturnsEmployeeByEmail() throws Exception {
        String email = "isabel@gmail.com";

        when(employeeService.findEmployeeByEmail(email)).thenReturn(EMPLOYEE);

        String expectedJson = objectMapper.writeValueAsString(EMPLOYEE);

        mockMvc.perform(get("/api/employees/find/by-email")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should create a new employee and returns Created")
    public void createEmployee_WithValidData_ReturnsEmployeeCreated() throws Exception {
        String employeeJson = objectMapper.writeValueAsString(EMPLOYEE_REQUEST_DTO);

        mockMvc
                .perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should replace an existing employee and returns No Content")
    public void replaceEmployee_WithValidData_ReturnsNoContent() throws Exception {
        String employeeJson = objectMapper.writeValueAsString(EMPLOYEE_UPDATE_DTO);

        mockMvc
                .perform(put("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should delete a employee by id and returns No Content")
    public void deleteEmployeeById_ReturnsNoContent() throws Exception {
        when(employeeService.findEmployeeById(EMPLOYEE.getId())).thenReturn(EMPLOYEE);

        mockMvc
                .perform(delete("/api/employees/{id}", EMPLOYEE.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

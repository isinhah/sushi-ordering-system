package com.sushi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Customer;
import com.sushi.api.security.TokenService;
import com.sushi.api.services.CustomerService;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.sushi.api.common.CustomerConstants.*;

import static com.sushi.api.common.CustomerControllerConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of customers inside page object when successful")
    public void listAllPageable_ReturnsAllCustomersWithPagination() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(CUSTOMERS_WITH_ADDRESS, pageable, CUSTOMERS_WITH_ADDRESS.size());

        when(customerService.listAllPageable(pageable)).thenReturn(page);

        String expectedJson = objectMapper.writeValueAsString(CUSTOMERS_WITH_ADDRESS);

        mockMvc
                .perform(get("/api/customers")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return an empty list of customers inside page object when there are no customers")
    void listAllPageable_ReturnsEmptyListOfCustomersInsidePageObject_WhenThereAreNoCustomers() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> emptyCustomerPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(customerService.listAllPageable(pageable)).thenReturn(emptyCustomerPage);

        mockMvc.perform(get("/api/customers")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of customers when successful")
    public void listAllNonPageable_ReturnsAllCustomers() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(CUSTOMERS_WITH_ADDRESS);

        when(customerService.listAllNonPageable()).thenReturn(CUSTOMERS_WITH_ADDRESS);

        mockMvc.perform(get("/api/customers/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a customer by id when successful")
    public void findCustomerById_ReturnsCustomerById() throws Exception {
        when(customerService.findCustomerById(CUSTOMER.getId())).thenReturn(CUSTOMER);

        String expectedJson = objectMapper.writeValueAsString(CUSTOMER);

        mockMvc.perform(get("/api/customers/{id}", CUSTOMER.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return ResourceNotFoundException when trying to find a customer by id that does not exist")
    public void findCustomerById_ReturnsNotFound_WhenCustomerDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(customerService.findCustomerById(nonExistentId)).thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of customers by name when successful")
    public void findCustomersByName_ReturnsListOfCustomers_WhenSuccessful() throws Exception {
        String name = "mar";
        List<Customer> customers = List.of(CUSTOMER3, CUSTOMER4);

        when(customerService.findCustomerByName(name)).thenReturn(customers);

        String expectedJson = objectMapper.writeValueAsString(customers);

        mockMvc.perform(get("/api/customers/find/by-name")
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a customer by email when successful")
    public void findCustomerByEmail_ReturnsCustomerByEmail_WhenSuccessful() throws Exception {
        String email = "isabel@gmail.com";

        when(customerService.findCustomerByEmail(email)).thenReturn(CUSTOMER);

        String expectedJson = objectMapper.writeValueAsString(CUSTOMER);

        mockMvc.perform(get("/api/customers/find/by-email")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should create a new customer and returns Created")
    public void createCustomer_WithValidData_ReturnsCustomerCreated() throws Exception {
        String employeeJson = objectMapper.writeValueAsString(CUSTOMER);

        mockMvc
                .perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should replace an existing customer and returns No Content")
    public void replaceCustomer_WithValidData_ReturnsNoContent() throws Exception {
        String employeeJson = objectMapper.writeValueAsString(CUSTOMER);

        mockMvc
                .perform(put("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should delete a customer by id and returns No Content")
    public void deleteCustomerById_ReturnsNoContent() throws Exception {
        when(customerService.findCustomerById(CUSTOMER.getId())).thenReturn(CUSTOMER);

        mockMvc
                .perform(delete("/api/customers/{id}", CUSTOMER.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
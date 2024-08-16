package com.sushi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sushi.api.security.TokenService;
import com.sushi.api.services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.sushi.api.common.AuthConstants.LOGIN_REQUEST_DTO;
import static com.sushi.api.common.AuthConstants.REGISTER_REQUEST_DTO;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AuthService authService;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Login customer with valid data should return token")
    public void loginCustomer_WithValidData_ReturnsToken() throws Exception {
        String customerJson = objectMapper.writeValueAsString(LOGIN_REQUEST_DTO);

        mockMvc
                .perform(post("/api/auth/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Register customer with valid data should return token")
    public void registerCustomer_WithValidData_ReturnsToken() throws Exception {
        String customerJson = objectMapper.writeValueAsString(REGISTER_REQUEST_DTO);

        mockMvc
                .perform(post("/api/auth/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Login employee with valid data should return token")
    public void loginEmployee_WithValidData_ReturnsToken() throws Exception {
        String customerJson = objectMapper.writeValueAsString(LOGIN_REQUEST_DTO);

        mockMvc
                .perform(post("/api/auth/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Register employee with valid data should return token")
    public void registerEmployee_WithValidData_ReturnsToken() throws Exception {
        String customerJson = objectMapper.writeValueAsString(REGISTER_REQUEST_DTO);

        mockMvc
                .perform(post("/api/auth/employees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}

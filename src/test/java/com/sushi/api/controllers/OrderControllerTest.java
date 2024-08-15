package com.sushi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Order;
import com.sushi.api.security.TokenService;
import com.sushi.api.services.OrderService;
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

import static com.sushi.api.common.OrderConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of orders inside page object when successful")
    public void listAllPageable_ReturnsAllOrdersWithPagination() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> page = new PageImpl<>(ORDERS, pageable, ORDERS.size());

        when(orderService.listAllPageable(pageable)).thenReturn(page);

        String expectedJson = objectMapper.writeValueAsString(ORDERS);

        mockMvc
                .perform(get("/api/orders")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should return a list of orders when successful")
    public void listAllNonPageable_ReturnsAllOrders() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(ORDERS);

        when(orderService.listAllNonPageable()).thenReturn(ORDERS);

        mockMvc.perform(get("/api/orders/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a order by id when successful")
    public void findOrderById_ReturnsOrderById() throws Exception {
        when(orderService.findOrderById(ORDER.getId())).thenReturn(ORDER);

        String expectedJson = objectMapper.writeValueAsString(ORDER);

        mockMvc.perform(get("/api/orders/{id}", ORDER.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return ResourceNotFoundException when trying to find a order by id that does not exist")
    public void findOrderById_ReturnsNotFound_WhenOrderDoesNotExist() throws Exception {
        when(orderService.findOrderById(5L)).thenThrow(new ResourceNotFoundException("Order not found"));

        mockMvc.perform(get("/api/orders/{id}", 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should create a new order and returns Created")
    public void createOrder_WithValidData_ReturnsOrderCreated() throws Exception {
        String orderJson = objectMapper.writeValueAsString(ORDER_REQUEST_DTO);

        mockMvc
                .perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should replace an existing order")
    public void replaceOrder_WithValidData() throws Exception {
        String orderJson = objectMapper.writeValueAsString(ORDER_UPDATE_DTO);

        mockMvc
                .perform(put("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should delete an order by id and returns No Content")
    public void deleteOrderById_ReturnsNoContent() throws Exception {
        when(orderService.findOrderById(ORDER.getId())).thenReturn(ORDER);

        mockMvc
                .perform(delete("/api/orders/{id}", ORDER.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

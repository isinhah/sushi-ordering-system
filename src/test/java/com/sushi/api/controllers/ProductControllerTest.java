package com.sushi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Product;
import com.sushi.api.security.TokenService;
import com.sushi.api.services.ProductService;
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

import java.util.List;

import static com.sushi.api.common.ProductConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a list of products inside page object when successful")
    public void listAllPageable_ReturnsAllProductsWithPagination() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(PRODUCTS, pageable, PRODUCTS.size());

        when(productService.listAllPageable(pageable)).thenReturn(page);

        String expectedJson = objectMapper.writeValueAsString(PRODUCTS);

        mockMvc
                .perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a list of products when successful")
    public void listAllNonPageable_ReturnsAllProducts() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(PRODUCTS);

        when(productService.listAllNonPageable()).thenReturn(PRODUCTS);

        mockMvc.perform(get("/api/products/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a product by id when successful")
    public void findProductById_ReturnsProductById() throws Exception {
        when(productService.findProductById(PRODUCT.getId())).thenReturn(PRODUCT);

        String expectedJson = objectMapper.writeValueAsString(PRODUCT);

        mockMvc.perform(get("/api/products/{id}", PRODUCT.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return ResourceNotFoundException when trying to find a product by id that does not exist")
    public void findProductById_ReturnsNotFound_WhenProductDoesNotExist() throws Exception {
        when(productService.findProductById(5L)).thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/{id}", 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a product by name when successful")
    public void findProductByName_ReturnsProductByName() throws Exception {
        String name = "roll";
        List<Product> products = List.of(PRODUCT, PRODUCT2);

        when(productService.findProductByName(name)).thenReturn(products);

        String expectedJson = objectMapper.writeValueAsString(products);

        mockMvc.perform(get("/api/products/find/by-name")
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should create a new product when successful")
    public void createProduct_WithValidData_ReturnsProductCreated() throws Exception {
        String productJson = objectMapper.writeValueAsString(PRODUCT);

        mockMvc
                .perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should replace a product by id when successful")
    public void replaceProduct_WithValidData_ReturnsNoContent() throws Exception {
        String productJson = objectMapper.writeValueAsString(PRODUCT);

        mockMvc
                .perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should delete a product by id and returns No Content")
    public void deleteProductById_ReturnsNoContent() throws Exception {
        when(productService.findProductById(PRODUCT.getId())).thenReturn(PRODUCT);

        mockMvc
                .perform(delete("/api/products/{id}", PRODUCT.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
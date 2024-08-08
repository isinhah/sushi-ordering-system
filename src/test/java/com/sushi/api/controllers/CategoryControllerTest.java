package com.sushi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Category;
import com.sushi.api.security.TokenService;
import com.sushi.api.services.CategoryService;
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

import static com.sushi.api.common.CategoryConstants.CATEGORIES;
import static com.sushi.api.common.CategoryConstants.CATEGORY;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private CategoryService categoryService;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a list of categories inside page object when successful")
    public void listAllPageable_ReturnsAllCategoriesWithPagination() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(CATEGORIES, pageable, CATEGORIES.size());

        when(categoryService.listAllPageable(pageable)).thenReturn(page);

        String expectedJson = objectMapper.writeValueAsString(CATEGORIES);

        mockMvc
                .perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a list of categories when successful")
    public void listAllNonPageable_ReturnsAllCategories() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(CATEGORIES);

        when(categoryService.listAllNonPageable()).thenReturn(CATEGORIES);

        mockMvc.perform(get("/api/categories/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a category by id when successful")
    public void findCategoryById_ReturnsCategoryById() throws Exception {
        when(categoryService.findCategoryById(CATEGORY.getId())).thenReturn(CATEGORY);

        String expectedJson = objectMapper.writeValueAsString(CATEGORY);

        mockMvc.perform(get("/api/categories/{id}", CATEGORY.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return ResourceNotFoundException when trying to find a category by id that does not exist")
    public void findCategoryById_ReturnsNotFound_WhenCategoryDoesNotExist() throws Exception {
        when(categoryService.findCategoryById(5L)).thenThrow(new ResourceNotFoundException("Category not found"));

        mockMvc.perform(get("/api/categories/{id}", 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Should return a list of categories when successful")
    public void findCategoryByName_ReturnsListOfCategories_WhenSuccessful() throws Exception {
        String name = "sushi";
        List<Category> categories = List.of(new Category("Sushi", "Delicious sushi"));

        when(categoryService.findCategoryByName(name)).thenReturn(categories);

        String expectedJson = objectMapper.writeValueAsString(categories);

        mockMvc.perform(get("/api/categories/find/by-name")
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should create a new category and returns Created")
    public void createCategory_WithValidData_ReturnsCategoryCreated() throws Exception {
        String categoryJson = objectMapper.writeValueAsString(CATEGORY);

        mockMvc
                .perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should replace an existing category and returns No Content")
    public void replaceCategory_WithValidData_ReturnsNoContent() throws Exception {
        String categoryJson = objectMapper.writeValueAsString(CATEGORY);

        mockMvc
                .perform(put("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should delete a category by id and returns No Content")
    public void deleteCustomerById_ReturnsNoContent() throws Exception {
        when(categoryService.findCategoryById(CATEGORY.getId())).thenReturn(CATEGORY);

        mockMvc
                .perform(delete("/api/categories/{id}", CATEGORY.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

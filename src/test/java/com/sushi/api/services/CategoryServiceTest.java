package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Category;
import com.sushi.api.model.dto.category.CategoryRequestDTO;
import com.sushi.api.model.dto.category.CategoryUpdateDTO;
import com.sushi.api.repositories.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sushi.api.common.CategoryConstants.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should return a list of categories inside page object when successful")
    void listAll_ReturnsListOfCategoriesInsidePageObject_WhenSuccessful() {
        Page<Category> categoryPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Page<Category> result = categoryService.listAllPageable(pageable);

        assertNotNull(result);
        assertEquals(categoryPage, result);
    }

    @Test
    @DisplayName("Should return an empty list of categories inside page object when there are no categories")
    void listAllPageable_ReturnsEmptyListOfCategoriesInsidePageObject_WhenThereAreNoCategories() {
        Page<Category> emptyCategoryPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = mock(Pageable.class);

        when(categoryRepository.findAll(pageable)).thenReturn(emptyCategoryPage);

        Page<Category> result = categoryService.listAllPageable(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of categories when successful")
    void listAllNonPageable_ReturnsListOfCategories_WhenSuccessful() {
        when(categoryRepository.findAll()).thenReturn(CATEGORIES);

        List<Category> result = categoryService.listAllNonPageable();

        assertNotNull(result);
        assertEquals(CATEGORIES.size(), result.size());
    }

    @Test
    @DisplayName("Should return an empty list of categories when there are no categories")
    void listAllNonPageable_ReturnsEmptyListOfCategories_WhenThereAreNoCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.listAllNonPageable();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return a category by id when successful")
    void findCategoryById_ReturnsCategory_WhenSuccessful() {
        when(categoryRepository.findById(CATEGORY.getId())).thenReturn(Optional.of(CATEGORY));

        Category result = categoryService.findCategoryById(CATEGORY.getId());

        assertNotNull(result);
        assertEquals(CATEGORY, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when category id does not exist")
    void findCategoryById_ThrowsResourceNotFoundException_WhenCategoryIdDoesNotExist() {
        when(categoryRepository.findById(CATEGORY.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findCategoryById(CATEGORY.getId()));
    }

    @Test
    @DisplayName("Should return a category by name when successful")
    void findCategoryByName_ReturnsCategory_WhenSuccessful() {
        String name = "sushi";
        List<Category> categories = List.of(CATEGORY, CATEGORY3);

        when(categoryRepository.findByNameContainingIgnoreCase(name)).thenReturn(categories);

        List<Category> result = categoryService.findCategoryByName(name);

        assertNotNull(result);
        assertEquals(categories, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when category name does not exist")
    void findCategoryByName_ThrowsResourceNotFoundException_WhenCategoryNameDoesNotExist() {
        String name = "doces";

        when(categoryRepository.findByNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findCategoryByName(CATEGORY.getName()));
    }

    @Test
    @DisplayName("Should create a new category when provided with valid CategoryRequestDTO")
    void createCategory_WithValidData_CreatesCategory() {
        CategoryRequestDTO request = new CategoryRequestDTO(CATEGORY.getName(), CATEGORY.getDescription());

        when(categoryRepository.save(any(Category.class))).thenReturn(CATEGORY);

        Category result = categoryService.createCategory(request);

        assertNotNull(result);
        assertEquals(CATEGORY.getName(), result.getName());
        assertEquals(CATEGORY.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw a DataIntegrityViolationException when name already exists")
    void createCategory_WithExistingName_ThrowsDataIntegrityViolationException() {
        CategoryRequestDTO request = new CategoryRequestDTO(CATEGORY.getName(), CATEGORY.getDescription());
        Category existingCategory = new Category(CATEGORY.getName(), CATEGORY.getDescription());

        when(categoryRepository.findByNameContainingIgnoreCase(request.name())).thenReturn(List.of(existingCategory));

        assertThrows(DataIntegrityViolationException.class, () -> categoryService.createCategory(request));
    }

    @Test
    @DisplayName("Should replace an existing category when provided with valid CategoryUpdateDTO")
    void replaceCategory_WhenSuccessful() {
        when(categoryRepository.findById(CATEGORY.getId())).thenReturn(Optional.of(CATEGORY));

        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO(
                CATEGORY.getId(),
                "newName",
                "newDescription"
        );

        categoryService.replaceCategory(updateDTO);

        verify(categoryRepository).findById(CATEGORY.getId());
        verify(categoryRepository).save(CATEGORY);
    }

    @Test
    @DisplayName("Should delete a category by id when successful")
    void deleteCategory_WithExistingId_WhenSuccessful() {
        when(categoryRepository.findById(CATEGORY.getId())).thenReturn(Optional.of(CATEGORY));

        assertThatCode(() -> categoryService.deleteCategory(CATEGORY.getId())).doesNotThrowAnyException();

        verify(categoryRepository, times(1)).delete(CATEGORY);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when category id does not exist")
    void deleteCategory_ThrowsResourceNotFoundException_WhenIdDoesNotExist() {
        when(categoryRepository.findById(CATEGORY.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(CATEGORY.getId()));
    }
}

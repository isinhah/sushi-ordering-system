package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Category;
import com.sushi.api.model.Product;
import com.sushi.api.model.dto.category.CategoryRequestDTO;
import com.sushi.api.model.dto.category.CategoryUpdateDTO;
import com.sushi.api.repositories.CategoryRepository;
import com.sushi.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> listAllNonPageable() {
        return categoryRepository.findAll();
    }

    public Page<Category> listAllPageable(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with this id."));
    }

    @Transactional
    public Category createCategory(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        return categoryRepository.save(category);
    }

    @Transactional
    public void replaceCategory(CategoryUpdateDTO dto) {
        Category category = findCategoryById(dto.id());

        category.setName(dto.name());
        category.setDescription(dto.description());

        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.delete(findCategoryById(id));
    }
}
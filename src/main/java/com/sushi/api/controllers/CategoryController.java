package com.sushi.api.controllers;

import com.sushi.api.model.Category;
import com.sushi.api.model.dto.category.CategoryRequestDTO;
import com.sushi.api.model.dto.category.CategoryUpdateDTO;
import com.sushi.api.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> listAllNonPageable() {
        return new ResponseEntity<>(categoryService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Category>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(categoryService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable Long id) {
        Category category = categoryService.findCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        return new ResponseEntity<>(categoryService.createCategory(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replaceCategory(@Valid @RequestBody CategoryUpdateDTO dto) {
        categoryService.replaceCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
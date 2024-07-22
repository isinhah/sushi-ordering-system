package com.sushi.api.controllers;

import com.sushi.api.model.Product;
import com.sushi.api.model.dto.product.ProductRequestDTO;
import com.sushi.api.model.dto.product.ProductUpdateDTO;
import com.sushi.api.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<List<Product>> listAllNonPageable() {
        return new ResponseEntity<>(productService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(productService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {
        Product category = productService.findProductById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replaceProduct(@Valid @RequestBody ProductUpdateDTO dto) {
        productService.replaceProduct(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
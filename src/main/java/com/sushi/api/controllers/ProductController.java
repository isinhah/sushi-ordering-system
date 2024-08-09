package com.sushi.api.controllers;

import com.sushi.api.model.Customer;
import com.sushi.api.model.Product;
import com.sushi.api.model.dto.product.ProductRequestDTO;
import com.sushi.api.model.dto.product.ProductUpdateDTO;
import com.sushi.api.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products", produces = {"application/json"})
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products (non-pageable)",
            description = "Retrieve a list of all products without pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/list")
    public ResponseEntity<List<Product>> listAllNonPageable() {
        return new ResponseEntity<>(productService.listAllNonPageable(), HttpStatus.OK);
    }

    @Operation(summary = "Get all products (pageable)",
            description = "Retrieve a paginated list of products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Product>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(productService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @Operation(summary = "Get product by ID",
            description = "Retrieve a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {
        Product product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get products by name",
            description = "Retrieve a list of products by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/find/by-name")
    public ResponseEntity<List<Product>> findProductByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findProductByName(name));
    }

    @Operation(summary = "Create a new product",
            description = "Create a new product with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing product",
            description = "Update an existing product with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<Void> replaceProduct(@Valid @RequestBody ProductUpdateDTO dto) {
        productService.replaceProduct(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a product by ID",
            description = "Delete a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
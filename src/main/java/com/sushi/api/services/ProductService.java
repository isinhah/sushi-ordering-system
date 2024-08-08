package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Category;
import com.sushi.api.model.Product;
import com.sushi.api.model.dto.product.ProductRequestDTO;
import com.sushi.api.model.dto.product.ProductUpdateDTO;
import com.sushi.api.repositories.CategoryRepository;
import com.sushi.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> listAllNonPageable() {
        return productRepository.findAll();
    }

    public Page<Product> listAllPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with this id."));
    }

    public List<Product> findProductByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found with this name.");
        }
        return products;
    }

    @Transactional
    public Product createProduct(ProductRequestDTO dto) {
        List<Product> existingProducts = productRepository.findByNameContainingIgnoreCase(dto.name());

        if (!existingProducts.isEmpty()) {
            throw new DataIntegrityViolationException("Data integrity violation error occurred.");
        }

        Product product = new Product();

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setPortionQuantity(dto.portionQuantity());
        product.setPortionUnit(dto.portionUnit());
        product.setUrlImage(dto.urlImage());

        Set<Category> categories = dto.categoriesId().stream()
                .map(id -> categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with this id.")))
                .collect(Collectors.toSet());
        product.setCategories(categories);

        return productRepository.save(product);
    }

    @Transactional
    public void replaceProduct(ProductUpdateDTO dto) {
        Product product = findProductById(dto.id());

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setPortionQuantity(dto.portionQuantity());
        product.setPortionUnit(dto.portionUnit());
        product.setUrlImage(dto.urlImage());

        Set<Category> categories = dto.categoriesId().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with this id.")))
                .collect(Collectors.toSet());
        product.setCategories(categories);

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.delete(findProductById(id));
    }
}
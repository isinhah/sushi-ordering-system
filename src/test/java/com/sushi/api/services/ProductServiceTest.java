package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Product;
import com.sushi.api.model.dto.product.ProductRequestDTO;
import com.sushi.api.model.dto.product.ProductUpdateDTO;
import com.sushi.api.repositories.CategoryRepository;
import com.sushi.api.repositories.ProductRepository;
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
import java.util.Set;

import static com.sushi.api.common.CategoryConstants.CATEGORY;
import static com.sushi.api.common.CategoryConstants.CATEGORY2;
import static com.sushi.api.common.ProductConstants.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should return a list of products inside page object when successful")
    void listAll_ReturnsListOfProductsInsidePageObject_WhenSuccessful() {
        Page<Product> productPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        Page<Product> result = productService.listAllPageable(pageable);

        assertNotNull(result);
        assertEquals(productPage, result);
    }

    @Test
    @DisplayName("Should return an empty list of products inside page object when there are no products")
    void listAllPageable_ReturnsEmptyListOfProductsInsidePageObject_WhenThereAreNoProducts() {
        Page<Product> emptyProductPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = mock(Pageable.class);

        when(productRepository.findAll(pageable)).thenReturn(emptyProductPage);

        Page<Product> result = productService.listAllPageable(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of products when successful")
    void listAllNonPageable_ReturnsListOfProducts_WhenSuccessful() {
        when(productRepository.findAll()).thenReturn(PRODUCTS);

        List<Product> result = productService.listAllNonPageable();

        assertNotNull(result);
        assertEquals(PRODUCTS.size(), result.size());
    }

    @Test
    @DisplayName("Should return an empty list of products when there are no products")
    void listAllNonPageable_ReturnsEmptyListOfProducts_WhenThereAreNoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> result = productService.listAllNonPageable();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return a product by id when successful")
    void findProductById_ReturnsProduct_WhenSuccessful() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

        Product result = productService.findProductById(PRODUCT.getId());

        assertNotNull(result);
        assertEquals(PRODUCT, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when product id does not exist")
    void findProductById_ThrowsResourceNotFoundException_WhenProductIdDoesNotExist() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findProductById(PRODUCT.getId()));
    }

    @Test
    @DisplayName("Should return a product by name when successful")
    void findProductByName_ReturnsProduct_WhenSuccessful() {
        String name = "roll";
        List<Product> products = List.of(PRODUCT, PRODUCT2);

        when(productRepository.findByNameContainingIgnoreCase(name)).thenReturn(products);

        List<Product> result = productService.findProductByName(name);

        assertNotNull(result);
        assertEquals(products, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when product name does not exist")
    void findProductByName_ThrowsResourceNotFoundException_WhenProductNameDoesNotExist() {
        String name = "vegetariano";

        when(productRepository.findByNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> productService.findProductByName(PRODUCT.getName()));
    }

    @Test
    @DisplayName("Should create a new product when provided with valid ProductRequestDTO")
    void createProduct_WithValidData_CreatesProduct() {
        ProductRequestDTO dto = new ProductRequestDTO(
                "newName", "newDescription", 10.49, 6, "pieces", "http://example.com/images/spicy_tuna_roll.jpg",
                Set.of(1L, 2L)
        );

        when(productRepository.findByNameContainingIgnoreCase(dto.name())).thenReturn(List.of());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(CATEGORY));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(CATEGORY2));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.createProduct(dto);

        verify(productRepository).findByNameContainingIgnoreCase(dto.name());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw a DataIntegrityViolationException when name already exists")
    void createProduct_WithExistingName_ThrowsDataIntegrityViolationException() {
        ProductRequestDTO request = new ProductRequestDTO(PRODUCT.getName(), PRODUCT.getDescription(), PRODUCT.getPrice(), PRODUCT.getPortionQuantity(), PRODUCT.getPortionUnit(), PRODUCT.getUrlImage(), Set.of(CATEGORY.getId(), CATEGORY2.getId()));
        Product existingProduct = new Product(2L, PRODUCT.getName(), PRODUCT.getDescription());

        when(productRepository.findByNameContainingIgnoreCase(request.name())).thenReturn(List.of(existingProduct));

        assertThrows(DataIntegrityViolationException.class, () -> productService.createProduct(request));
    }

    @Test
    @DisplayName("Should replace an existing product when provided with valid ProductUpdateDTO")
    void replaceProduct_WhenSuccessful() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        when(categoryRepository.findById(CATEGORY.getId())).thenReturn(Optional.of(CATEGORY));
        when(categoryRepository.findById(CATEGORY2.getId())).thenReturn(Optional.of(CATEGORY2));

        ProductUpdateDTO updateDTO = new ProductUpdateDTO(
                PRODUCT.getId(),
                "newName", "newDescription", 10.49, 6, "pieces", "http://example.com/images/spicy_tuna_roll.jpg",
                Set.of(CATEGORY.getId(), CATEGORY2.getId())
        );

        productService.replaceProduct(updateDTO);

        verify(productRepository).findById(PRODUCT.getId());
        verify(categoryRepository).findById(CATEGORY.getId());
        verify(categoryRepository).findById(CATEGORY2.getId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete a product by id when successful")
    void deleteProduct_WithExistingId_WhenSuccessful() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

        assertThatCode(() -> productService.deleteProduct(PRODUCT.getId())).doesNotThrowAnyException();

        verify(productRepository, times(1)).delete(PRODUCT);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when product id does not exist")
    void deleteProduct_ThrowsResourceNotFoundException_WhenIdDoesNotExist() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(PRODUCT.getId()));
    }
}
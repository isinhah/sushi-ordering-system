package com.sushi.api.common;

import com.sushi.api.model.Product;

import java.util.List;

import static com.sushi.api.common.CategoryConstants.CATEGORIES_FOR_PRODUCTS;

public class ProductConstants {
    public static final Product PRODUCT = new Product(1L, "California Roll",
            "A delicious roll made with crab meat, avocado, and cucumber.",
            8.99,
            8,
            "pieces",
            "http://example.com/images/california_roll.jpg", CATEGORIES_FOR_PRODUCTS);
    public static final Product PRODUCT2 = new Product(2L, "Spicy Tuna Roll",
            "A flavorful roll made with spicy tuna, cucumber, and a hint of sriracha.",
            10.49,
            6,
            "pieces",
            "http://example.com/images/spicy_tuna_roll.jpg", CATEGORIES_FOR_PRODUCTS);
    public static final List<Product> PRODUCTS = List.of(PRODUCT, PRODUCT2);
}
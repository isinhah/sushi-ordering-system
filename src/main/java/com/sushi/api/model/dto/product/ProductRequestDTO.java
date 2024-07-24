package com.sushi.api.model.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;


public record ProductRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Description cannot be blank")
        String description,
        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than zero")
        Double price,
        @NotNull(message = "Quantity of portions cannot be null")
        Integer portionQuantity,
        @NotBlank(message = "Units of portions cannot be blank")
        String portionUnit,
        @NotBlank(message = "URL Image cannot be blank")
        String urlImage,
        Set<Long> categoriesId
) {
}
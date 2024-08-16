package com.sushi.api.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

@Schema(name = "Product Request DTO", description = "DTO for creating a product")
public record ProductRequestDTO(
        @Schema(description = "The name of the product", example = "California Roll")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Schema(description = "A description of the product", example = "A delicious roll made with crab meat, avocado, and cucumber")
        @NotBlank(message = "Description cannot be blank")
        String description,

        @Schema(description = "The price of the product", example = "8.99")
        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than zero")
        Double price,

        @Schema(description = "The quantity of portions in the product", example = "20")
        @NotNull(message = "Quantity of portions cannot be null")
        Integer portionQuantity,

        @Schema(description = "The unit of measurement for portions", example = "pieces")
        @NotBlank(message = "Units of portions cannot be blank")
        String portionUnit,

        @Schema(description = "The URL of the product's image", example = "http://example.com/images/california_roll.jpg")
        @NotBlank(message = "URL Image cannot be blank")
        String urlImage,

        @Schema(description = "A set of category IDs associated with the product (it can be null)", example = "[]")
        Set<Long> categoriesId
) {
}
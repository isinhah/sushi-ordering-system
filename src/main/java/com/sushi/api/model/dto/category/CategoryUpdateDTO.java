package com.sushi.api.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Category Update DTO", description = "DTO for updating a category")
public record CategoryUpdateDTO(
        @Schema(description = "The unique identifier of the category", example = "1")
        @NotNull(message = "ID cannot be null")
        Long id,
        @Schema(description = "The category name", example = "Japanese desserts")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(description = "The category description", example = "Various types of desserts")
        @NotBlank(message = "Description cannot be blank")
        String description
) {}
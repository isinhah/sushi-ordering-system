package com.sushi.api.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Category Request DTO", description = "DTO for creating a category")
public record CategoryRequestDTO(
        @Schema(description = "The category name", example = "Japanese desserts")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(description = "The category description", example = "Various types of desserts")
        @NotBlank(message = "Description cannot be blank")
        String description
) {}
package com.sushi.api.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryUpdateDTO(
        @NotNull(message = "ID cannot be null")
        Long id,
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Description cannot be blank")
        String description
) {}
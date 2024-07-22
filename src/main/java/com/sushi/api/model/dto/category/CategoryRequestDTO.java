package com.sushi.api.model.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Description cannot be blank")
        String description
) {}
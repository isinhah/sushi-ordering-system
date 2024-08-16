package com.sushi.api.model.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Login Request DTO", description = "DTO for user login request")
public record LoginRequestDTO(
        @Schema(description = "The user's email", example = "ana@example.com")
        @NotBlank(message = "Email is mandatory")
        String email,

        @Schema(description = "The user's password", example = "123")
        @NotBlank(message = "Password is mandatory")
        String password
) {}
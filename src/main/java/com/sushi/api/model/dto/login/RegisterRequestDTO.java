package com.sushi.api.model.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Register Request DTO", description = "DTO for register a user")
public record RegisterRequestDTO(
        @Schema(description = "The user's name", example = "Clara")
        @NotBlank(message = "Name is mandatory") String name,

        @Schema(description = "The user's email", example = "clara@example.com")
        @NotBlank(message = "Email is mandatory") String email,

        @Schema(description = "The user's password", example = "123")
        @NotBlank(message = "Password is mandatory") String password) {
}
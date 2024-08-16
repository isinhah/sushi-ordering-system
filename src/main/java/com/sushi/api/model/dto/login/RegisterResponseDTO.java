package com.sushi.api.model.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Register Response DTO", description = "Response DTO for user registration")
public record RegisterResponseDTO(String name, String token, String expiresAt) {
}
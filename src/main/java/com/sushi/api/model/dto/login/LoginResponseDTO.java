package com.sushi.api.model.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Login Response DTO", description = "Response DTO for user login")
public record LoginResponseDTO(String name, String token, String expiresAt) {
}
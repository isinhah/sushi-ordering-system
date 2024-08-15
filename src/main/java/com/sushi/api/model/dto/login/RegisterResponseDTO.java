package com.sushi.api.model.dto.login;

public record RegisterResponseDTO(String name, String token, String expiresAt) {
}
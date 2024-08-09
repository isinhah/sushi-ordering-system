package com.sushi.api.model.dto.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank(message = "Email is mandatory") String email, @NotBlank(message = "Password is mandatory") String password) {
}

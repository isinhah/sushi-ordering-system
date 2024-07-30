package com.sushi.api.model.dto.employee;

import jakarta.validation.constraints.NotBlank;

public record EmployeeRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
package com.sushi.api.model.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Employee Request DTO", description = "DTO for creating an employee")
public record EmployeeRequestDTO(
        @Schema(description = "The employee's name", example = "Laura")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(description = "The employee's email", example = "laura@example.com")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @Schema(description = "The employee's password", example = "123")
        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
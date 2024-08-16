package com.sushi.api.model.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "Employee Update DTO", description = "DTO for updating an employee")
public record EmployeeUpdateDTO(
        @Schema(description = "The unique identifier of the employee", example = "550e7200-e29b-41d4-a716-446655440000")
        @NotNull(message = "ID cannot be null")
        UUID id,
        @Schema(description = "The employee's name", example = "Laura Silva")
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
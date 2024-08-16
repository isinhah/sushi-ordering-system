package com.sushi.api.model.dto.customer;

import com.sushi.api.model.dto.address.AddressDTO;
import com.sushi.api.model.dto.phone.PhoneDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(name = "Customer Request DTO", description = "DTO for creating a customer")
public record CustomerRequestDTO(
        @Schema(description = "The customer's name", example = "Alice")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(description = "The customer's email", example = "alice@example.com")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @Schema(description = "The customer's password", example = "123")
        @NotBlank(message = "Password cannot be blank")
        String password,
        @Schema(description = "The customer's phone number", example = "123456789")
        @NotNull(message = "Phone cannot be null")
        @Valid
        PhoneDTO phone,
        @Schema(description = "The customer's address")
        @NotNull(message = "Address cannot be null")
        Set<@Valid AddressDTO> addresses
) {}
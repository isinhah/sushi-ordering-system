package com.sushi.api.model.dto.customer;

import com.sushi.api.model.dto.address.AddressDTO;
import com.sushi.api.model.dto.phone.PhoneDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

@Schema(name = "Customer Update DTO", description = "DTO for updating a customer")
public record CustomerUpdateDTO(
        @Schema(description = "The unique identifier of the customer", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "ID cannot be null")
        UUID id,
        @Schema(description = "The customer's name", example = "Alice Silva")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(description = "The customer's email", example = "alice@example.com")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @Schema(description = "The customer's password", example = "123")
        @NotBlank(message = "Password cannot be blank")
        String password,
        @Schema(description = "The customer's phone number", example = "1234567890")
        @NotNull(message = "Phone cannot be null")
        @Valid
        PhoneDTO phone,
        @Schema(description = "The customer's address")
        @NotNull(message = "Address cannot be null")
        Set<@Valid AddressDTO> addresses
) {}
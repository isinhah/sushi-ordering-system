package com.sushi.api.model.dto.address;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        @NotEmpty(message = "Address number cannot be empty")
        String number,
        @NotEmpty(message = "Street cannot be empty")
        @Size(max = 255, message = "Street must be less than 255 characters")
        String street,
        @NotEmpty(message = "Neighborhood cannot be empty")
        @Size(max = 255, message = "Neighborhood must be less than 255 characters")
        String neighborhood
) {}
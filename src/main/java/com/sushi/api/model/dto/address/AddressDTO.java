package com.sushi.api.model.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        @NotBlank(message = "Address number cannot be blank")
        String number,
        @NotBlank(message = "Street cannot be blank")
        @Size(max = 255, message = "Street must be less than 255 characters")
        String street,
        @NotBlank(message = "Neighborhood cannot be blank")
        @Size(max = 255, message = "Neighborhood must be less than 255 characters")
        String neighborhood
) {}
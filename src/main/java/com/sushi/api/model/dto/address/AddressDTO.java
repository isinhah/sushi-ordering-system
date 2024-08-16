package com.sushi.api.model.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Address DTO", description = "DTO for Address")
public record AddressDTO(
        @Schema(description = "The address number", example = "123")
        @NotBlank(message = "Address number cannot be blank")
        String number,

        @Schema(description = "The street name", example = "Avenida São João")
        @NotBlank(message = "Street cannot be blank")
        @Size(max = 255, message = "Street must be less than 255 characters")
        String street,

        @Schema(description = "The neighborhood name", example = "Centro")
        @NotBlank(message = "Neighborhood cannot be blank")
        @Size(max = 255, message = "Neighborhood must be less than 255 characters")
        String neighborhood
) {}
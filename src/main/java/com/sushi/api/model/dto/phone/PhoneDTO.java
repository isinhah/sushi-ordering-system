package com.sushi.api.model.dto.phone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Phone DTO", description = "DTO for phone number")
public record PhoneDTO(
        @Schema(description = "Phone number", example = "1234567890")
        @NotBlank(message = "Phone number cannot be null")
        String number
) {}
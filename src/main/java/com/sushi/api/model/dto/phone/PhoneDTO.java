package com.sushi.api.model.dto.phone;

import jakarta.validation.constraints.NotBlank;

public record PhoneDTO(
        @NotBlank(message = "Phone number cannot be null")
        String number
) {}
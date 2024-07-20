package com.sushi.api.model.dto.phone;

import jakarta.validation.constraints.NotEmpty;

public record PhoneDTO(
        @NotEmpty(message = "Phone number cannot be empty")
        String number
) {}
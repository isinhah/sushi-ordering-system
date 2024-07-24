package com.sushi.api.model.dto.order_item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemUpdateDTO(
        Long id,
        @NotNull(message = "Product ID cannot be null")
        Long productId,

        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}
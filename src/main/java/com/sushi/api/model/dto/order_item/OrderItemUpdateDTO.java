package com.sushi.api.model.dto.order_item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "Order Item Update DTO", description = "update order item DTO")
public record OrderItemUpdateDTO(
        @Schema(description = "The unique identifier of the order item", example = "1")
        Long id,
        @Schema(description = "The unique identifier of the product", example = "1")
        @NotNull(message = "Product ID cannot be null")
        Long productId,

        @Schema(description = "The quantity of the product", example = "2")
        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}
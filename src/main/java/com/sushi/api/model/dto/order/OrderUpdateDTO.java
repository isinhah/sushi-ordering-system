package com.sushi.api.model.dto.order;

import com.sushi.api.model.dto.order_item.OrderItemUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "Order Update DTO", description = "DTO for updating a order")
public record OrderUpdateDTO(
        @Schema(description = "The unique identifier of the order", example = "1")
        @NotNull(message = "Order ID cannot be null")
        Long id,

        @Schema(description = "ID of the delivery address", example = "1")
        @NotNull(message = "Delivery Address ID cannot be null")
        Long deliveryAddressId,

        @Schema(description = "Order items")
        @NotNull(message = "Order items cannot be null")
        List<@Valid OrderItemUpdateDTO> items
) {}
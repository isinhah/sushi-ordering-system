package com.sushi.api.model.dto.order;

import com.sushi.api.model.dto.order_item.OrderItemUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderUpdateDTO(
        @NotNull(message = "Order ID cannot be null")
        Long id,

        @NotNull(message = "Delivery Address ID cannot be null")
        Long deliveryAddressId,

        @NotNull(message = "Order items cannot be null")
        List<@Valid OrderItemUpdateDTO> items
) {}
package com.sushi.api.model.dto.order;

import com.sushi.api.model.dto.order_item.OrderItemRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
        @NotNull(message = "Customer ID cannot be null")
        UUID customerId,

        @NotNull(message = "Delivery Address ID cannot be null")
        Long deliveryAddressId,

        @NotNull(message = "Order items cannot be null")
        List<@Valid OrderItemRequestDTO> items
) {}
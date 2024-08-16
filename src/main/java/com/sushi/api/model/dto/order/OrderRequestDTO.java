package com.sushi.api.model.dto.order;

import com.sushi.api.model.dto.order_item.OrderItemRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(name = "Order Request DTO", description = "DTO for creating a order")
public record OrderRequestDTO(
        @Schema(description = "ID of the customer placing the order", example = "d13c75d3-e7ed-4b6a-8f89-cc7a1d4a2c76")
        @NotNull(message = "Customer ID cannot be null")
        UUID customerId,

        @Schema(description = "ID of the delivery address", example = "123")
        @NotNull(message = "Delivery Address ID cannot be null")
        Long deliveryAddressId,

        @Schema(description = "Order items")
        @NotNull(message = "Order items cannot be null")
        List<@Valid OrderItemRequestDTO> items
) {}
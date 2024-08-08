package com.sushi.api.common;

import com.sushi.api.model.Order;
import com.sushi.api.model.OrderItem;
import com.sushi.api.model.dto.order.OrderRequestDTO;
import com.sushi.api.model.dto.order.OrderUpdateDTO;
import com.sushi.api.model.dto.order_item.OrderItemRequestDTO;
import com.sushi.api.model.dto.order_item.OrderItemUpdateDTO;

import java.util.List;

import static com.sushi.api.common.CustomerConstants.ADDRESS;
import static com.sushi.api.common.CustomerConstants.CUSTOMER;
import static com.sushi.api.common.ProductConstants.PRODUCT;

public class OrderConstants {
    public static final List<OrderItem> ITEMS = List.of(new OrderItem(1L, 2, 8.99));
    public static final OrderItem ORDER_ITEM = new OrderItem(1L, 1, 10.00);

    public static final Order ORDER = new Order(1L, CUSTOMER, ADDRESS, ITEMS);
    public static final List<Order> ORDERS = List.of(ORDER);

    public static final OrderItemRequestDTO ORDER_ITEM_REQUEST_DTO = new OrderItemRequestDTO(PRODUCT.getId(), 2);
    public static final OrderItemUpdateDTO ORDER_ITEM_UPDATE_DTO = new OrderItemUpdateDTO(1L, PRODUCT.getId(), 2);
    public static final OrderRequestDTO ORDER_REQUEST_DTO = new OrderRequestDTO(CUSTOMER.getId(), ADDRESS.getId(), List.of(ORDER_ITEM_REQUEST_DTO));
    public static final OrderUpdateDTO ORDER_UPDATE_DTO = new OrderUpdateDTO(ORDER.getId(), ADDRESS.getId(), List.of(ORDER_ITEM_UPDATE_DTO));
}

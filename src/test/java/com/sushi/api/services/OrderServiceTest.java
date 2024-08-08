package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Order;
import com.sushi.api.model.dto.order.OrderRequestDTO;
import com.sushi.api.model.dto.order.OrderUpdateDTO;
import com.sushi.api.repositories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sushi.api.common.CustomerConstants.*;
import static com.sushi.api.common.CustomerConstants.CUSTOMER;
import static com.sushi.api.common.OrderConstants.*;
import static com.sushi.api.common.ProductConstants.PRODUCT;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("Should return a list of orders inside page object when successful")
    void listAll_ReturnsListOfOrdersInsidePageObject_WhenSuccessful() {
        Page<Order> orderPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        Page<Order> result = orderService.listAllPageable(pageable);

        assertNotNull(result);
        assertEquals(orderPage, result);
    }

    @Test
    @DisplayName("Should return an empty list of orders inside page object when there are no orders")
    void listAllPageable_ReturnsEmptyListOfOrdersInsidePageObject_WhenThereAreNoOrders() {
        Page<Order> emptyOrderPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = mock(Pageable.class);

        when(orderRepository.findAll(pageable)).thenReturn(emptyOrderPage);

        Page<Order> result = orderService.listAllPageable(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of orders when successful")
    void listAllNonPageable_ReturnsListOfOrders_WhenSuccessful() {
        when(orderRepository.findAll()).thenReturn(ORDERS);

        List<Order> result = orderService.listAllNonPageable();

        assertNotNull(result);
        assertEquals(ORDERS.size(), result.size());
    }

    @Test
    @DisplayName("Should return an empty list of orders when there are no orders")
    void listAllNonPageable_ReturnsEmptyListOfOrders_WhenThereAreNoOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> result = orderService.listAllNonPageable();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return an order by id when successful")
    void findOrderById_ReturnsOrder_WhenSuccessful() {
        when(orderRepository.findById(ORDER.getId())).thenReturn(Optional.of(ORDER));

        Order result = orderService.findOrderById(ORDER.getId());

        assertNotNull(result);
        assertEquals(ORDER, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when order id does not exist")
    void findOrderById_ThrowsResourceNotFoundException_WhenOrderIdDoesNotExist() {
        when(orderRepository.findById(ORDER.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.findOrderById(ORDER.getId()));
    }

    @Test
    @DisplayName("Should create a new order when provided with valid OrderRequestDTO")
    void createOrder_WithValidData_CreatesOrder() {
        OrderRequestDTO request = new OrderRequestDTO(CUSTOMER.getId(), ADDRESS.getId(), List.of(ORDER_ITEM_REQUEST_DTO));

        when(customerRepository.findById(CUSTOMER.getId())).thenReturn(Optional.of(CUSTOMER_ADDRESS));
        when(addressRepository.findById(ADDRESS.getId())).thenReturn(Optional.of(ADDRESS));
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        when(orderRepository.save(any(Order.class))).thenReturn(ORDER);

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(ORDER.getId(), result.getId());
        assertEquals(ORDER.getDeliveryAddress(), result.getDeliveryAddress());
        assertEquals(ORDER.getItems(), result.getItems());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should replace an existing order when provided with valid OrderUpdateDTO")
    void replaceOrder_WhenSuccessful() {
        OrderUpdateDTO updateDTO = new OrderUpdateDTO(PRODUCT.getId(), ADDRESS.getId(), List.of(ORDER_ITEM_UPDATE_DTO));

        when(orderRepository.findById(ORDER.getId())).thenReturn(Optional.of(ORDER));
        when(addressRepository.findById(ADDRESS.getId())).thenReturn(Optional.of(ADDRESS));
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        when(orderItemRepository.findById(ORDER_ITEM.getId())).thenReturn(Optional.of(ORDER_ITEM));
        when(orderRepository.save(any(Order.class))).thenReturn(ORDER);

        Order result = orderService.replaceOrder(updateDTO);

        assertNotNull(result);
        assertEquals(ORDER.getId(), result.getId());

        verify(orderRepository).findById(ORDER.getId());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should delete an order by id when successful")
    void deleteOrder_WithExistingId_WhenSuccessful() {
        when(orderRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(ORDER));

        assertThatCode(() -> orderService.deleteOrder(ORDER.getId())).doesNotThrowAnyException();

        verify(orderRepository, times(1)).delete(ORDER);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when order id does not exist")
    void deleteOrder_ThrowsResourceNotFoundException_WhenIdDoesNotExist() {
        when(orderRepository.findById(ORDER.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(ORDER.getId()));
    }
}
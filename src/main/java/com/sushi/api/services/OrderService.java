package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.*;
import com.sushi.api.model.dto.order.OrderRequestDTO;
import com.sushi.api.model.dto.order.OrderUpdateDTO;
import com.sushi.api.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, AddressRepository addressRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> listAllNonPageable() {
        return orderRepository.findAll();
    }

    public Page<Order> listAllPageable(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with this id."));
    }

    @Transactional
    public Order createOrder(OrderRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with this id."));
        Address address = addressRepository.findById(dto.deliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with this id."));

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setDeliveryAddress(address);

        List<OrderItem> items = dto.items().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with this id."));
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setPrice(product.getPrice());
            item.calculateTotalPrice();
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        order.calculateTotalAmount();

        return orderRepository.save(order);
    }

    @Transactional
    public Order replaceOrder(OrderUpdateDTO dto) {
        Order order = orderRepository.findById(dto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with this id."));

        Address address = addressRepository.findById(dto.deliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with this id."));
        order.setDeliveryAddress(address);

        List<OrderItem> items = dto.items().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with this id."));
            OrderItem item;
            if (itemDto.id() != null) {
                item = orderItemRepository.findById(itemDto.id())
                        .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with this id."));
            } else {
                item = new OrderItem();
                item.setOrder(order);
            }
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setPrice(product.getPrice());
            item.calculateTotalPrice();
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        order.calculateTotalAmount();

        return orderRepository.save(order);
    }


    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.delete(findOrderById(id));
    }
}

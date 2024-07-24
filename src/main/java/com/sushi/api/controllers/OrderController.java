package com.sushi.api.controllers;

import com.sushi.api.model.Order;
import com.sushi.api.model.dto.order.OrderRequestDTO;
import com.sushi.api.model.dto.order.OrderUpdateDTO;
import com.sushi.api.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public ResponseEntity<List<Order>> listAllNonPageable() {
        return new ResponseEntity<>(orderService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Order>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(orderService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id) {
        Order category = orderService.findOrderById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return new ResponseEntity<>(orderService.createOrder(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Order> replaceOrder(@Valid @RequestBody OrderUpdateDTO dto) {
        orderService.replaceOrder(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
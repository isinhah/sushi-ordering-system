package com.sushi.api.controllers;

import com.sushi.api.model.Customer;
import com.sushi.api.model.dto.customer.CustomerRequestDTO;
import com.sushi.api.model.dto.customer.CustomerUpdateDTO;
import com.sushi.api.services.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(customerService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Customer>> listAllNonPageable() {
        return new ResponseEntity<>(customerService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable UUID id) {
        Customer customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }



    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        return new ResponseEntity<>(customerService.createCustomer(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replaceCustomer(@Valid @RequestBody CustomerUpdateDTO dto) {
        customerService.replaceCustomer(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
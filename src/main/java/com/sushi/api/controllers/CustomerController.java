package com.sushi.api.controllers;

import com.sushi.api.model.Customer;
import com.sushi.api.model.dto.customer.CustomerRequestDTO;
import com.sushi.api.model.dto.customer.CustomerUpdateDTO;
import com.sushi.api.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/customers", produces = {"application/json"})
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customers (pageable)",
            description = "Returns a paginated list of customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Customer>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(customerService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @Operation(summary = "Get all customers (non-pageable)",
            description = "Returns a list of all customers without pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/list")
    public ResponseEntity<List<Customer>> listAllNonPageable() {
        return new ResponseEntity<>(customerService.listAllNonPageable(), HttpStatus.OK);
    }

    @Operation(summary = "Get customer by ID",
            description = "Returns a customer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable UUID id) {
        Customer customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Find customers by name",
            description = "Returns a list of customers matching the given name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No customers found with the given name"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/find/by-name")
    public ResponseEntity<List<Customer>> findCustomerByName(@RequestParam String name) {
        return ResponseEntity.ok(customerService.findCustomerByName(name));
    }

    @Operation(summary = "Find customer by email",
            description = "Returns a customer by its email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/find/by-email")
    public ResponseEntity<Customer> findCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.findCustomerByEmail(email));
    }

    @Operation(summary = "Create a new customer",
            description = "Create a new customer with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        return new ResponseEntity<>(customerService.createCustomer(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing customer",
            description = "Update an existing customer with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<Void> replaceCustomer(@Valid @RequestBody CustomerUpdateDTO dto) {
        customerService.replaceCustomer(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a customer by ID",
            description = "Delete an existing customer by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
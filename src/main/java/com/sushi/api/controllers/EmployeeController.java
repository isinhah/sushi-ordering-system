package com.sushi.api.controllers;

import com.sushi.api.model.Employee;
import com.sushi.api.model.dto.employee.EmployeeRequestDTO;
import com.sushi.api.model.dto.employee.EmployeeUpdateDTO;
import com.sushi.api.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> listAllPageable(Pageable pageable) {
        return new ResponseEntity<>(employeeService.listAllPageable(pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Employee>> listAllNonPageable() {
        return new ResponseEntity<>(employeeService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable UUID id) {
        Employee employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/find/by-email")
    public ResponseEntity<Employee> findEmployeeByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employeeService.findEmployeeByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        return new ResponseEntity<>(employeeService.createEmployee(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replaceEmployee(@Valid @RequestBody EmployeeUpdateDTO dto) {
        employeeService.replaceEmployee(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
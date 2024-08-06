package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Address;
import com.sushi.api.model.Customer;
import com.sushi.api.model.Phone;
import com.sushi.api.model.dto.customer.CustomerRequestDTO;
import com.sushi.api.model.dto.customer.CustomerUpdateDTO;
import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.LoginResponseDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;
import com.sushi.api.repositories.CustomerRepository;
import com.sushi.api.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    public Page<Customer> listAllPageable(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public List<Customer> listAllNonPageable() {
        return customerRepository.findAll();
    }

    public Customer findCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with this id."));
    }

    public List<Customer> findCustomerByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("No customers found with this name.");
        }
        return customers;
    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Customer not found with this id."));
    }

    public LoginResponseDTO loginCustomer(LoginRequestDTO dto) {
        Customer customer = customerRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        if (passwordEncoder.matches(dto.password(), customer.getPassword())) {
            String token = tokenService.generateCustomerToken(customer);
            return new LoginResponseDTO(customer.getName(), token);
        }

        return new LoginResponseDTO("Invalid credentials", null);
    }

    @Transactional
    public LoginResponseDTO registerCustomer(RegisterRequestDTO dto) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(dto.email());
        if (existingCustomer.isEmpty()) {
            Customer newCustomer = new Customer();
            newCustomer.setPassword(passwordEncoder.encode(dto.password()));
            newCustomer.setEmail(dto.email());
            newCustomer.setName(dto.name());

            customerRepository.save(newCustomer);
            String token = tokenService.generateCustomerToken(newCustomer);
            return new LoginResponseDTO(newCustomer.getName(), token);
        }

        return new LoginResponseDTO("Customer already exists", null);
    }

    @Transactional
    public Customer createCustomer(CustomerRequestDTO dto) {
        if (customerRepository.findByEmail(dto.email()).isPresent()) {
            throw new DataIntegrityViolationException("Data integrity violation error occurred.");
        }

        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPassword(dto.password());

        Phone phone = new Phone();
        phone.setNumber(dto.phone().number());
        phone.setCustomer(customer);
        customer.setPhone(phone);

        Set<Address> addresses = dto.addresses().stream()
                .map(addressDTO -> {
                    Address address = new Address();
                    address.setNumber(addressDTO.number());
                    address.setStreet(addressDTO.street());
                    address.setNeighborhood(addressDTO.neighborhood());
                    address.setCustomer(customer);
                    return address;
                })
                .collect(Collectors.toSet());

        customer.setAddresses(addresses);

        return customerRepository.save(customer);
    }

    @Transactional
    public void replaceCustomer(CustomerUpdateDTO dto) {
        Customer savedCustomer = findCustomerById(dto.id());
        savedCustomer.setName(dto.name());
        savedCustomer.setEmail(dto.email());
        savedCustomer.setPassword(dto.password());

        Phone phone = savedCustomer.getPhone();
        if (phone == null) {
            phone = new Phone();
            phone.setCustomer(savedCustomer);
        }
        phone.setNumber(dto.phone().number());
        savedCustomer.setPhone(phone);

        savedCustomer.getAddresses().clear();
        if (dto.addresses() != null) {
            Set<Address> updatedAddresses = dto.addresses().stream()
                    .map(addressDTO -> {
                        Address address = new Address();
                        address.setNumber(addressDTO.number());
                        address.setStreet(addressDTO.street());
                        address.setNeighborhood(addressDTO.neighborhood());
                        address.setCustomer(savedCustomer);
                        return address;
                    })
                    .collect(Collectors.toSet());
            savedCustomer.getAddresses().addAll(updatedAddresses);
        }

        customerRepository.save(savedCustomer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        customerRepository.delete(findCustomerById(id));
    }
}
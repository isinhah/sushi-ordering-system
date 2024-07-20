package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Address;
import com.sushi.api.model.Customer;
import com.sushi.api.model.Phone;
import com.sushi.api.model.dto.customer.CustomerRequestDTO;
import com.sushi.api.model.dto.customer.CustomerUpdateDTO;
import com.sushi.api.repositories.AddressRepository;
import com.sushi.api.repositories.CustomerRepository;
import com.sushi.api.repositories.PhoneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Customer createCustomer(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());

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
        Optional<Customer> savedCustomerOpt = customerRepository.findById(dto.id());

        if (!customerRepository.existsById(dto.id())) {
            throw new ResourceNotFoundException("Customer not found with this ID.");
        }

        Customer savedCustomer = savedCustomerOpt.get();
        savedCustomer.setName(dto.name());
        savedCustomer.setEmail(dto.email());

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
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with this ID.");
        }
        customerRepository.deleteById(id);
    }
}
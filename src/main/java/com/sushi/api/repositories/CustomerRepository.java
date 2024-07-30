package com.sushi.api.repositories;

import com.sushi.api.model.Customer;
import com.sushi.api.model.Phone;
import com.sushi.api.model.dto.phone.PhoneDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByNameContainingIgnoreCase(String name);
    Optional<Customer> findByEmail(String email);
}
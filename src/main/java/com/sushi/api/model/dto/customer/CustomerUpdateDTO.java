package com.sushi.api.model.dto.customer;

import com.sushi.api.model.dto.address.AddressDTO;
import com.sushi.api.model.dto.phone.PhoneDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CustomerUpdateDTO(
        @NotNull(message = "ID cannot be null")
        UUID id,
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Email cannot be empty")
        String email,
        @NotNull(message = "Phone cannot be null")
        PhoneDTO phone,
        @NotNull(message = "Address cannot be null")
        Set<@Valid AddressDTO> addresses
) {}

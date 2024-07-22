package com.sushi.api.model.dto.customer;

import com.sushi.api.model.dto.address.AddressDTO;
import com.sushi.api.model.dto.phone.PhoneDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CustomerUpdateDTO(
        @NotNull(message = "ID cannot be null")
        UUID id,
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotNull(message = "Phone cannot be null")
        @Valid
        PhoneDTO phone,
        @NotNull(message = "Address cannot be null")
        Set<@Valid AddressDTO> addresses
) {}

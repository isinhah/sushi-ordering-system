package com.sushi.api.common;

import com.sushi.api.model.Customer;
import com.sushi.api.model.dto.customer.CustomerRequestDTO;
import com.sushi.api.model.dto.customer.CustomerUpdateDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.sushi.api.common.CustomerConstants.*;

public class CustomerControllerConstants {
    public static final Customer CUSTOMER_WITH_ADDRESS = new Customer(UUID.randomUUID(), "isabel", "isabel@gmail.com", "1234", PHONE);
    public static final List<Customer> CUSTOMERS_WITH_ADDRESS = List.of(CUSTOMER_WITH_ADDRESS);
    static {
        CUSTOMER_WITH_ADDRESS.setAddresses(Set.of((ADDRESS)));
    }

    public static final CustomerRequestDTO CUSTOMER_REQUEST_DTO = new CustomerRequestDTO("isabel", "isabel@gmail.com", "1234", PHONE_DTO, Set.of(ADDRESS_DTO));
    public static final CustomerUpdateDTO CUSTOMER_UPDATE_DTO = new CustomerUpdateDTO(UUID.randomUUID(), "isabel", "isabel@gmail.com", "1234", PHONE_DTO, Set.of(ADDRESS_DTO));
}

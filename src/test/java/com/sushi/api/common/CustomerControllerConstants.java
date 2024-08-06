package com.sushi.api.common;

import com.sushi.api.model.Customer;

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
}

package com.sushi.api.common;

import com.sushi.api.model.Address;
import com.sushi.api.model.Customer;
import com.sushi.api.model.Phone;
import com.sushi.api.model.dto.address.AddressDTO;
import com.sushi.api.model.dto.phone.PhoneDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CustomerConstants {
    public static final String EMAIL = "ana@gmail.com";
    public static final String PASSWORD = "senha123";
    public static final String ENCODED_PASSWORD = "senhacodificada";
    public static final String TOKEN = "token";

    public static final Phone PHONE = new Phone("111111111");
    public static final PhoneDTO PHONE_DTO = new PhoneDTO("222222222");
    public static final Address ADDRESS = new Address(1L, "123", "Main St", "Downtown");
    public static final AddressDTO ADDRESS_DTO = new AddressDTO("123", "Main St", "Downtown");

    public static final Customer CUSTOMER_LOGIN = new Customer(UUID.randomUUID(), "ana", EMAIL, PASSWORD, PHONE);
    public static final Customer CUSTOMER_ADDRESS = new Customer(UUID.randomUUID(), "pedro", "pedro@gmail.com", "1234", PHONE);
    public static final Customer CUSTOMER = new Customer(UUID.randomUUID(), "isabel", "isabel@gmail.com", "1234", PHONE);
    public static final Customer CUSTOMER2 = new Customer(UUID.randomUUID(), "joao", "joao@gmail.com", "1234", PHONE);
    public static final Customer CUSTOMER3 = new Customer(UUID.randomUUID(), "maria", "maria@gmail.com", "1234", PHONE);
    public static final Customer CUSTOMER4 = new Customer(UUID.randomUUID(), "mariana", "mariana@gmail.com", "1234", PHONE);
    public static final List<Customer> CUSTOMERS = List.of(CUSTOMER2, CUSTOMER3, CUSTOMER4);
    static {
        CUSTOMER_ADDRESS.setAddresses(Set.of((ADDRESS)));
    }
}
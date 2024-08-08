package com.sushi.api.services;

import com.sushi.api.exceptions.ResourceNotFoundException;
import com.sushi.api.model.Customer;
import com.sushi.api.model.dto.customer.CustomerRequestDTO;
import com.sushi.api.model.dto.customer.CustomerUpdateDTO;
import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.LoginResponseDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;
import com.sushi.api.repositories.CustomerRepository;
import com.sushi.api.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static com.sushi.api.common.CustomerConstants.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a list of customers inside page object when successful")
    void listAll_ReturnsListOfCustomersInsidePageObject_WhenSuccessful() {
        Page<Customer> customerPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        Page<Customer> result = customerService.listAllPageable(pageable);

        assertNotNull(result);
        assertEquals(customerPage, result);
    }

    @Test
    @DisplayName("Should return an empty list of customers inside page object when there are no customers")
    void listAllPageable_ReturnsEmptyListOfCustomersInsidePageObject_WhenThereAreNoCustomers() {
        Page<Customer> emptyCustomerPage = new PageImpl<>(Collections.emptyList());
        Pageable pageable = mock(Pageable.class);

        when(customerRepository.findAll(pageable)).thenReturn(emptyCustomerPage);

        Page<Customer> result = customerService.listAllPageable(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of customers when successful")
    void listAllNonPageable_ReturnsListOfCustomers_WhenSuccessful() {
        when(customerRepository.findAll()).thenReturn(CUSTOMERS);

        List<Customer> result = customerService.listAllNonPageable();

        assertNotNull(result);
        assertEquals(CUSTOMERS.size(), result.size());
    }

    @Test
    @DisplayName("Should return an empty list of customers when there are no customers")
    void listAllNonPageable_ReturnsEmptyListOfCustomers_WhenThereAreNoCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Customer> result = customerService.listAllNonPageable();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return a customer by id when successful")
    void findCustomerById_ReturnsCustomer_WhenSuccessful() {
        when(customerRepository.findById(CUSTOMER.getId())).thenReturn(Optional.of(CUSTOMER));

        Customer result = customerService.findCustomerById(CUSTOMER.getId());

        assertNotNull(result);
        assertEquals(CUSTOMER, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when customer id does not exist")
    void findCustomerById_ThrowsResourceNotFoundException_WhenCustomerIdDoesNotExist() {
        when(customerRepository.findById(CUSTOMER.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.findCustomerById(CUSTOMER.getId()));
    }

    @Test
    @DisplayName("Should return a list of customers by name when successful")
    void findCustomerByName_ReturnsListOfCustomers_WhenSuccessful() {
        String name = "mar";
        List<Customer> customers = List.of(CUSTOMER3, CUSTOMER4);

        when(customerRepository.findByNameContainingIgnoreCase(name)).thenReturn(customers);

        List<Customer> result = customerService.findCustomerByName(name);

        assertNotNull(result);
        assertEquals(customers, result);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when there are no customers with the expected name")
    void findCustomerByName_ReturnsResourceNotFoundException_WhenThereAreNoCustomersWithTheName() {
        String name = "joao";

        when(customerRepository.findByNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> customerService.findCustomerByName(CUSTOMER.getName()));
    }

    @Test
    @DisplayName("Should return a customer by email when successful")
    void findCustomerByEmail_ReturnsCustomer_WhenSuccessful() {
        String email = "isabel@gmail.com";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(CUSTOMER));

        Customer result = customerService.findCustomerByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when customer email does not exist")
    void findCustomerByEmail_ThrowsResourceNotFoundException_WhenCustomerEmailDoesNotExist() {
        String email = "joao@gmail.com";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(CUSTOMER2));

        Customer result = customerService.findCustomerByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with a token when credentials are valid")
    void loginCustomer_WithValidCredentials_ReturnsToken() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER_LOGIN));
        when(passwordEncoder.matches(PASSWORD, CUSTOMER_LOGIN.getPassword())).thenReturn(true);
        when(tokenService.generateCustomerToken(CUSTOMER_LOGIN)).thenReturn(TOKEN);

        LoginResponseDTO response = customerService.loginCustomer(request);

        assertNotNull(response);
        assertEquals(CUSTOMER_LOGIN.getName(), response.name());
        assertEquals(TOKEN, response.token());
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when password is invalid")
    void loginCustomer_WithInvalidPassword_ReturnsInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, "senhaerrada");

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER_LOGIN));
        when(passwordEncoder.matches("senhaerrada", CUSTOMER_LOGIN.getPassword())).thenReturn(false);

        LoginResponseDTO response = customerService.loginCustomer(request);

        assertNotNull(response);
        assertEquals("Invalid credentials", response.name());
        assertNull(response.token());
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when customer does not exist")
    void loginCustomer_WithNonExistingCustomer_ThrowsResourceNotFoundException() {
        LoginRequestDTO request = new LoginRequestDTO(EMAIL, PASSWORD);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.loginCustomer(request));
    }

    @Test
    @DisplayName("Should register a new customer and return a LoginRequestDTO with a valid token")
    void registerCustomer_SavesNewCustomer_ReturnsToken() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(tokenService.generateCustomerToken(any(Customer.class))).thenReturn(TOKEN);

        LoginResponseDTO response = customerService.registerCustomer(request);

        assertNotNull(response);
        assertEquals("ana", response.name());
        assertEquals(TOKEN, response.token());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should return a LoginResponseDTO with invalid credentials message when customer already exists")
    void registerCustomer_WithExistingCustomer_ReturnsInvalidCredentials() {
        RegisterRequestDTO request = new RegisterRequestDTO("ana", EMAIL, PASSWORD);

        when(customerRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new Customer()));

        LoginResponseDTO response = customerService.registerCustomer(request);

        assertNotNull(response);
        assertEquals("Customer already exists", response.name());
        assertNull(response.token());

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should create a new customer when provided with valid CustomerRequestDTO")
    void createCustomer_WithValidData_CreatesCustomer() {
        CustomerRequestDTO request = new CustomerRequestDTO(CUSTOMER_ADDRESS.getName(), CUSTOMER_ADDRESS.getEmail(), CUSTOMER_ADDRESS.getPassword(), PHONE_DTO, Set.of(ADDRESS_DTO));

        when(customerRepository.save(any(Customer.class))).thenReturn(CUSTOMER_ADDRESS);

        Customer result = customerService.createCustomer(request);

        assertNotNull(result);
        assertEquals(CUSTOMER_ADDRESS.getName(), result.getName());
        assertEquals(CUSTOMER_ADDRESS.getEmail(), result.getEmail());
        assertEquals(CUSTOMER_ADDRESS.getPassword(), result.getPassword());
        assertEquals(PHONE.getNumber(), result.getPhone().getNumber());
        assertEquals(1, result.getAddresses().size());
        assertEquals(ADDRESS.getNumber(), result.getAddresses().iterator().next().getNumber());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw a DataIntegrityViolationException when email already exists")
    void createCustomer_WithExistingEmail_ThrowsDataIntegrityViolationException() {
        CustomerRequestDTO request = new CustomerRequestDTO(CUSTOMER_ADDRESS.getName(), CUSTOMER_ADDRESS.getEmail(), CUSTOMER_ADDRESS.getPassword(), PHONE_DTO, Set.of(ADDRESS_DTO));

        when(customerRepository.findByEmail(request.email())).thenReturn(Optional.of(CUSTOMER_ADDRESS));

        assertThrows(DataIntegrityViolationException.class, () -> customerService.createCustomer(request));
    }

    @Test
    @DisplayName("Should replace an existing customer when provided with valid CustomerUpdateDTO")
    void replaceCustomer_WhenSuccessful() {
        when(customerRepository.findById(CUSTOMER.getId())).thenReturn(Optional.of(CUSTOMER));

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                CUSTOMER.getId(),
                "newName",
                "newEmail",
                "newPassword",
                PHONE_DTO,
                Set.of(ADDRESS_DTO)
        );

        customerService.replaceCustomer(updateDTO);

        verify(customerRepository).findById(CUSTOMER.getId());
        verify(customerRepository).save(CUSTOMER);
    }

    @Test
    @DisplayName("Should delete a customer by id when successful")
    void deleteCustomer_WithExistingId_WhenSuccessful() {
        when(customerRepository.findById(CUSTOMER.getId())).thenReturn(Optional.of(CUSTOMER));

        assertThatCode(() -> customerService.deleteCustomer(CUSTOMER.getId())).doesNotThrowAnyException();

        verify(customerRepository, times(1)).delete(CUSTOMER);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when customer id does not exist")
    void deleteCustomer_ThrowsResourceNotFoundException_WhenIdDoesNotExist() {
        when(customerRepository.findById(CUSTOMER.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(CUSTOMER.getId()));
    }
}
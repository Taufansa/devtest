package com.taufansamudra.devtest.services;

import com.taufansamudra.devtest.dto.CustomerDto;
import com.taufansamudra.devtest.models.Customer;
import com.taufansamudra.devtest.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceClass {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private BigInteger id;

    private UUID accountId;

    @BeforeEach
    public void setup() {
        accountId = UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f");
        id = BigInteger.valueOf(1);
        customer = Customer.builder()
                .id(id)
                .name("test 1")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();
    }

    @DisplayName("Test success to return list of customer based on created by")
    @Test
    public void testFindAllByCreatedBy_SuccessToReturnCustomerBasedOnCreatedBy() {
        var customer2 = Customer.builder()
                .id(id)
                .name("test 2")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();

        when(customerRepository.findAllByCreatedBy(accountId)).thenReturn(List.of(customer, customer2));

        var response = customerService.findAllByCreatedBy(accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", List.of(customer, customer2));
        expectedResponse.put("message", "customers fetched");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test success to return empty list of customer based on created by")
    @Test
    public void testFindAllByCreatedBy_SuccessToReturnEmptyCustomerBasedOnCreatedBy() {

        when(customerRepository.findAllByCreatedBy(accountId)).thenReturn(null);

        var response = customerService.findAllByCreatedBy(accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "no customers found");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test success to return customer detail based on customer id and created by")
    @Test
    public void testFindByIdAndCreatedBy_SuccessToReturnCustomerDetailBasedOnIdAndCreatedBy() {

        when(customerRepository.findByIdAndCreatedBy(id, accountId)).thenReturn(customer);

        var response = customerService.findByIdAndCreatedBy(id, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", customer);
        expectedResponse.put("message", "customer detail from " + customer.getName());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test error not found to return customer detail based on customer id and created by")
    @Test
    public void testFindByIdAndCreatedBy_ErrorNotFoundToReturnCustomerDetailBasedOnIdAndCreatedBy() {

        when(customerRepository.findByIdAndCreatedBy(id, accountId)).thenReturn(null);

        var response = customerService.findByIdAndCreatedBy(id, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "no customer found");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test success create new customer")
    @Test
    public void testCreateCustomer_SuccessCreateNewCustomer(){
        var newCustomer = Customer.builder()
                .id(null)
                .name("test 1")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();

        var createCustomerDto = CustomerDto.builder()
                .id(null)
                .name("test 1")
                .phone("083873360540")
                .build();

        when(customerRepository.saveAndFlush(newCustomer)).thenReturn(customer);
        when(customerRepository.findByNameAndCreatedBy(createCustomerDto.getName(), accountId)).thenReturn(null);


        var response = customerService.createCustomer(createCustomerDto, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", customer);
        expectedResponse.put("message", "new customer created");

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test error create customer and failed because name already used")
    @Test
    public void testCreateCustomer_ErrorCreateCustomer(){
        var newCustomer = Customer.builder()
                .id(null)
                .name("test 1")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();

        var createCustomerDto = CustomerDto.builder()
                .id(null)
                .name("test 1")
                .phone("083873360540")
                .build();
        when(customerRepository.saveAndFlush(newCustomer)).thenReturn(customer);
        when(customerRepository.findByNameAndCreatedBy(createCustomerDto.getName(), accountId)).thenReturn(customer);

        var response = customerService.createCustomer(createCustomerDto, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "customer name already used");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test success to update customer record")
    @Test
    public void testUpdateCustomer_SuccessUpdateCustomer(){

        var customerToUpdate = Customer.builder()
                .id(id)
                .name("test update")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();

        customer.setName("test update");

        var updateCustomerDto = CustomerDto.builder()
                .id(id)
                .name("test update")
                .phone("083873360540")
                .build();

        when(customerRepository.findByIdAndCreatedBy(updateCustomerDto.getId(), accountId)).thenReturn(customer);
        when(customerRepository.findByNameAndCreatedBy(updateCustomerDto.getName(), accountId)).thenReturn(null);
        when(customerRepository.saveAndFlush(customerToUpdate)).thenReturn(customer);


        var response = customerService.updateCustomer(updateCustomerDto, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", customer);
        expectedResponse.put("message", "customer updated");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test error to update customer record when name already used")
    @Test
    public void testUpdateCustomer_ErrorUpdateCustomerWhenNameAlreadyUsed(){

        var customerToUpdate = Customer.builder()
                .id(id)
                .name("test update")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();

        customer.setName("test update");

        var updateCustomerDto = CustomerDto.builder()
                .id(id)
                .name("test update")
                .phone("083873360540")
                .build();

        when(customerRepository.findByIdAndCreatedBy(updateCustomerDto.getId(), accountId)).thenReturn(customer);
        when(customerRepository.findByNameAndCreatedBy(updateCustomerDto.getName(), accountId)).thenReturn(customer);
        when(customerRepository.saveAndFlush(customerToUpdate)).thenReturn(customer);


        var response = customerService.updateCustomer(updateCustomerDto, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "customer name already used");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test error to update customer record when name record not found")
    @Test
    public void testUpdateCustomer_ErrorUpdateCustomerWhenRecordNotFound(){

        var customerToUpdate = Customer.builder()
                .id(id)
                .name("test update")
                .phone("083873360540")
                .createdBy(UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f"))
                .build();

        customer.setName("test update");

        var updateCustomerDto = CustomerDto.builder()
                .id(id)
                .name("test update")
                .phone("083873360540")
                .build();

        when(customerRepository.findByIdAndCreatedBy(updateCustomerDto.getId(), accountId)).thenReturn(null);
        when(customerRepository.findByNameAndCreatedBy(updateCustomerDto.getName(), accountId)).thenReturn(customer);
        when(customerRepository.saveAndFlush(customerToUpdate)).thenReturn(customer);


        var response = customerService.updateCustomer(updateCustomerDto, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "customer not found");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test success destroy customer record")
    @Test
    public void testDestroyCustomer_SuccessDestroyCustomer() {
        when(customerRepository.findByIdAndCreatedBy(id,accountId)).thenReturn(customer);

        var response = customerService.deleteCustomer(id, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "customer deleted");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

    @DisplayName("Test error destroy customer record when record not found")
    @Test
    public void testDestroyCustomer_ErrorDestroyCustomerWhenRecordNotFound() {
        when(customerRepository.findByIdAndCreatedBy(id,accountId)).thenReturn(null);

        var response = customerService.deleteCustomer(id, accountId);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", null);
        expectedResponse.put("message", "customer not found");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }
}

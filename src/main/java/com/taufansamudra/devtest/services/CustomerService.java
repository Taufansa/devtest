package com.taufansamudra.devtest.services;

import com.taufansamudra.devtest.dto.CustomerDto;
import com.taufansamudra.devtest.models.Customer;
import com.taufansamudra.devtest.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public ResponseEntity<Object> findAllByCreatedBy(UUID createdBy) {
        Map<String, Object> resultMap = new HashMap<>();

        var customers = customerRepository.findAllByCreatedBy(createdBy);

        resultMap.put("data", customers);
        if (Objects.isNull(customers)) {
            resultMap.put("message", "no customers found");
        } else {
            resultMap.put("message", "customers fetched");
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);

    }

    public ResponseEntity<Object> findByIdAndCreatedBy(BigInteger id, UUID createdBy) {
        Map<String, Object> resultMap = new HashMap<>();

        var customer = customerRepository.findByIdAndCreatedBy(id, createdBy);

        resultMap.put("data", customer);
        if (Objects.isNull(customer)) {
            resultMap.put("message", "no customer found");
            return new ResponseEntity(resultMap, HttpStatus.NOT_FOUND);
        } else {
            resultMap.put("message", "customer detail from " + customer.getName());
            return new ResponseEntity(resultMap, HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> createCustomer(CustomerDto customerDto, UUID createdBy) {
        Map<String, Object> resultMap = new HashMap<>();

        var checkCustomer = customerRepository.findByNameAndCreatedBy(customerDto.getName(), createdBy);

        if (!Objects.isNull(checkCustomer)) {
            resultMap.put("data", null);
            resultMap.put("message", "customer name already used");
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }

        var customer = Customer.builder()
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .createdBy(createdBy)
                .build();

        resultMap.put("data", customerRepository.saveAndFlush(customer));
        resultMap.put("message", "new customer created");

        return new ResponseEntity(resultMap, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updateCustomer(CustomerDto customerDto, UUID createdBy) {
        Map<String, Object> resultMap = new HashMap<>();

        var customer = customerRepository.findByIdAndCreatedBy(customerDto.getId(), createdBy);

        if (Objects.isNull(customer)) {
            resultMap.put("data", null);
            resultMap.put("message", "customer not found");
            return new ResponseEntity(resultMap, HttpStatus.NOT_FOUND);
        }

        var checkCustomerName = customerRepository.findByNameAndCreatedBy(customerDto.getName(), createdBy);

        if (!Objects.isNull(checkCustomerName)) {
            resultMap.put("data", null);
            resultMap.put("message", "customer name already used");
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }

        customer.setName(customerDto.getName());
        customer.setPhone(customerDto.getPhone());

        resultMap.put("data", customerRepository.saveAndFlush(customer));
        resultMap.put("message", "customer updated");

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteCustomer(BigInteger userId, UUID createdBy) {
        Map<String, Object> resultMap = new HashMap<>();

        var customer = customerRepository.findByIdAndCreatedBy(userId, createdBy);

        if (Objects.isNull(customer)) {
            resultMap.put("data", null);
            resultMap.put("message", "customer not found");
            return new ResponseEntity(resultMap, HttpStatus.NOT_FOUND);
        }

        customerRepository.delete(customer);

        resultMap.put("data", null);
        resultMap.put("message", "customer deleted");

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}

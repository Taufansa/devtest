package com.taufansamudra.devtest.controllers.v1;

import com.taufansamudra.devtest.dto.CustomerDto;
import com.taufansamudra.devtest.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(name = "USER-ID") UUID userId) {
        return customerService.findAllByCreatedBy(userId);
    }

    @GetMapping("/detail/{customerId}")
    public ResponseEntity<Object> findById(@RequestHeader(name = "USER-ID") UUID userId,
                                           @PathVariable("customerId") BigInteger customerId) {
        return customerService.findByIdAndCreatedBy(customerId, userId);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCustomer(@RequestHeader(name = "USER-ID") UUID userId,
                                                 @RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto, userId);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCustomer(@RequestHeader(name = "USER-ID") UUID userId,
                                                 @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(customerDto, userId);
    }

    @DeleteMapping("/destroy/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@RequestHeader(name = "USER-ID") UUID userId,
                                                 @PathVariable("customerId") BigInteger customerId) {
        return customerService.deleteCustomer(customerId, userId);
    }
}

package com.taufansamudra.devtest.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taufansamudra.devtest.dto.CustomerDto;
import com.taufansamudra.devtest.services.CustomerService;
import com.taufansamudra.devtest.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(name = "Authorization") String token) throws JsonProcessingException {
        var accountId = jwtUtil.payloadToMap(token).get("bv").toString();
        return customerService.findAllByCreatedBy(UUID.fromString(accountId));
    }

    @GetMapping("/detail/{customerId}")
    public ResponseEntity<Object> findById(@RequestHeader(name = "Authorization") String token,
                                           @PathVariable("customerId") BigInteger customerId) throws JsonProcessingException {
        var accountId = jwtUtil.payloadToMap(token).get("bv").toString();
        return customerService.findByIdAndCreatedBy(customerId, UUID.fromString(accountId));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCustomer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestBody CustomerDto customerDto) throws JsonProcessingException {
        var accountId = jwtUtil.payloadToMap(token).get("bv").toString();
        return customerService.createCustomer(customerDto, UUID.fromString(accountId));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCustomer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestBody CustomerDto customerDto) throws JsonProcessingException {
        var accountId = jwtUtil.payloadToMap(token).get("bv").toString();
        return customerService.updateCustomer(customerDto, UUID.fromString(accountId));
    }

    @DeleteMapping("/destroy/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@RequestHeader(name = "Authorization") String token,
                                                 @PathVariable("customerId") BigInteger customerId) throws JsonProcessingException {
        var accountId = jwtUtil.payloadToMap(token).get("bv").toString();
        return customerService.deleteCustomer(customerId, UUID.fromString(accountId));
    }
}

package com.taufansamudra.devtest.repositories;

import com.taufansamudra.devtest.models.Customer;
import org.hibernate.type.UUIDCharType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, BigInteger> {

    List<Customer> findAllByCreatedBy(UUID createdBy);

    Customer findByIdAndCreatedBy(BigInteger id, UUID createdBy);

    Customer findByNameAndCreatedBy(String name, UUID createdBy);

}

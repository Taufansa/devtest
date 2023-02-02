package com.taufansamudra.devtest.repositories;

import com.taufansamudra.devtest.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByUsername(String username);
}

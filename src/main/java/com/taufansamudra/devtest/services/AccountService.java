package com.taufansamudra.devtest.services;

import com.taufansamudra.devtest.dto.AccountDto;
import com.taufansamudra.devtest.models.Account;
import com.taufansamudra.devtest.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Object> createAccount(AccountDto accountDto) {
        var encoder = new BCryptPasswordEncoder();

        Map<String, Object> resultMap = new HashMap<>();

        var checkUserByUsername = accountRepository.findByUsername(accountDto.getUsername());

        if (!Objects.isNull(checkUserByUsername)) {
            resultMap.put("data", null);
            resultMap.put("message", "username already used");
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }

        var newAccount = Account.builder()
                .username(accountDto.getUsername())
                .password(encoder.encode(accountDto.getPassword()))
                .fullName(accountDto.getFullName())
                .build();

        resultMap.put("data", accountRepository.saveAndFlush(newAccount));
        resultMap.put("message", "account created");

        return new ResponseEntity(resultMap, HttpStatus.CREATED);
    }

}

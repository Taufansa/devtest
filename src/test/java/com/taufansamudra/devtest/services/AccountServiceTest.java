package com.taufansamudra.devtest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taufansamudra.devtest.dto.AccountDto;
import com.taufansamudra.devtest.models.Account;
import com.taufansamudra.devtest.repositories.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private Account accountCreate;
    private UUID id;

    @BeforeEach
    public void setup() {
        var encoder = new BCryptPasswordEncoder();

        id = UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f");

        accountCreate = Account.builder()
                .id(null)
                .username("testUSER")
                .fullName("test user ya")
                .password(encoder.encode("test"))
                .build();

        account = Account.builder()
                .id(id)
                .username("testUSER")
                .fullName("test user ya")
                .password(encoder.encode("test"))
                .build();
    }

    @DisplayName("Test success to create new account")
    @Test
    public void testCreateAccount_SuccessCreateAccount() {
        when(accountRepository.saveAndFlush(accountCreate)).thenReturn(account);

        AccountDto accountDto = AccountDto.builder()
                .id(null)
                .username("testUSER")
                .fullName("test user ya")
                .password("test")
                .build();

        var response = accountService.createAccount(accountDto);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("data", account);
        expectedResponse.put("message", "account created");

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        //Assertions.assertEquals(expectedResponse.toString(), response.getBody().toString());
    }

}

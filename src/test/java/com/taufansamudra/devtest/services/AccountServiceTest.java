package com.taufansamudra.devtest.services;

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

import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private UUID id;

    @BeforeEach
    public void setup() {
        id = UUID.fromString("2518e18d-099f-4628-945e-0e1468d8443f");
        account = Account.builder()
                .id(id)
                .username("testUSER")
                .password("test")
                .build();
    }

    @DisplayName("Test create new account")
    @Test
    public void testCreateAccount(){
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        AccountDto accountDto = AccountDto.builder()
                .id(null)
                .username("testUSER")
                .password("test")
                .build();

        Assertions.assertEquals(HttpStatus.CREATED, accountService.createAccount(accountDto).getStatusCode());
    }

}

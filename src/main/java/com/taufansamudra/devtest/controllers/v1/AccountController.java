package com.taufansamudra.devtest.controllers.v1;

import com.taufansamudra.devtest.dto.AccountDto;
import com.taufansamudra.devtest.dto.AuthRequest;
import com.taufansamudra.devtest.models.Account;
import com.taufansamudra.devtest.services.AccountService;
import com.taufansamudra.devtest.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/account/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public String auth(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return jwtUtil.generateToken(authRequest.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

}

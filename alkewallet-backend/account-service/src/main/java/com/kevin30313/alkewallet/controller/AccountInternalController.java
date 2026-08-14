package com.kevin30313.alkewallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kevin30313.alkewallet.service.AccountService;

@RestController
@RequestMapping("/api/internal/accounts")
public class AccountInternalController {

    private final AccountService accountService;

    public AccountInternalController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createAccount(@RequestParam Long userId) {
        accountService.createAccountForUser(userId);
        return ResponseEntity.ok().build();
    }
}
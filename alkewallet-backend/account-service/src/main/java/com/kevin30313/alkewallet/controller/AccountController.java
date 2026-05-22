package com.kevin30313.alkewallet.controller;

import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/internal/create")
    public ResponseEntity<?> createAccount(@RequestParam Long userId) {
        if (accountRepository.findByUserId(userId).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya tiene una cuenta.");
        }

        // Saldo inicial en cero para la billetera cyber-neon
        Account newAccount = new Account(userId, BigDecimal.ZERO, "CLP");
        Account savedAccount = accountRepository.save(newAccount);

        return ResponseEntity.ok(savedAccount);
    }
}
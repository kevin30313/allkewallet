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

    // --- 1. ENDPOINT PARA CREAR CUENTA (POST) ---
    @PostMapping("/internal/create")
    public ResponseEntity<?> createAccount(@RequestParam Long userId) {
        // Validación: si ya existe, detenemos la ejecución aquí
        if (accountRepository.findByUserId(userId).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya tiene una cuenta.");
        }

        // Saldo inicial en cero para la billetera cyber-neon
        Account newAccount = new Account(userId, BigDecimal.ZERO, "CLP");
        Account savedAccount = accountRepository.save(newAccount);

        return ResponseEntity.ok(savedAccount);
    } // <- Aquí se cierra limpiamente el método createAccount

    // --- 2. ENDPOINT PARA CONSULTAR CUENTA (GET) ---
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountByUserId(@PathVariable Long userId) {
        return accountRepository.findByUserId(userId)
                .map(account -> ResponseEntity.ok(account))
                .orElse(ResponseEntity.notFound().build()); 
                // Devuelve 200 OK si existe, o 404 si no encuentra la cuenta
    } // <- Aquí se cierra el método getAccountByUserId

} // <- Esta llave cierra la clase AccountController final
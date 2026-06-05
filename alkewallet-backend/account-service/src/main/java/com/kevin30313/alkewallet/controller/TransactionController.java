package com.kevin30313.alkewallet.controller;

import com.kevin30313.alkewallet.model.Transaction;
import com.kevin30313.alkewallet.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AccountService accountService;

    // Inyección por constructor limpia
    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Obtener el historial completo de movimientos de un usuario (tanto enviados como recibidos)
     * GET http://localhost:8080/api/transactions/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long userId) {
        List<Transaction> history = accountService.getTransactionHistory(userId);
        return ResponseEntity.ok(history);
    }
}

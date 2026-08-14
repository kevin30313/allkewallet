package com.kevin30313.alkewallet.controller;

import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.model.Transaction;
import com.kevin30313.alkewallet.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/me")
    public ResponseEntity<Account> getMyAccount() {
        return ResponseEntity.ok(accountService.getAccountByUserId(currentUserId()));
    }

    @GetMapping("/me/transactions")
    public ResponseEntity<List<Transaction>> getMyTransactions() {
        return ResponseEntity.ok(accountService.getTransactionHistory(currentUserId()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody Map<String, BigDecimal> body) {
        return ResponseEntity.ok(accountService.deposit(currentUserId(), body.get("amount")));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody Map<String, Object> body) {
        Long destinationUserId = Long.valueOf(body.get("destinationUserId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        accountService.transfer(currentUserId(), destinationUserId, amount);
        return ResponseEntity.ok().build();
    }
}

package com.kevin30313.alkewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.service.AccountService;

@RestController
@RequestMapping("/api/accounts/internal")
public class AccountInternalController {

    @Autowired
    private AccountService accountService;

    /**
     * Endpoint interno invocado por el auth-service mediante WebClient.
     * Recibe el userId por parámetro y genera su cuenta financiera.
     */
    @PostMapping("/create")
    public ResponseEntity<Void> createAccount(@RequestParam Long userId) {
        accountService.createAccountForUser(userId);
        // Respondemos un 200 OK vacío para notificar éxito al auth-service
        return ResponseEntity.ok().build();
    }
}
package com.kevin30313.alkewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.repository.AccountRepository;
import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Crea una billetera asociada al ID de usuario enviado por el auth-service.
     * Si ya existe una, la retorna para evitar duplicar datos.
     */
    @Transactional
    public Account createAccountForUser(Long userId) {
        return accountRepository.findByUserId(userId)
            .orElseGet(() -> {
                // Creamos la cuenta con saldo inicial de 10.000 CLP de bienvenida
                Account newAccount = new Account(userId, new BigDecimal("10000.00"), "CLP");
                return accountRepository.save(newAccount);
            });
    }
}
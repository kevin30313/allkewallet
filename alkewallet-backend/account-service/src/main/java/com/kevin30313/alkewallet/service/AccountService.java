package com.kevin30313.alkewallet.service;

import com.kevin30313.alkewallet.exception.InsufficientBalanceException;
import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.model.Transaction;
import com.kevin30313.alkewallet.repository.AccountRepository;
import com.kevin30313.alkewallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createAccountForUser(Long userId) {
        return accountRepository.findByUserId(userId)
            .orElseGet(() -> {
                Account newAccount = new Account(userId, new BigDecimal("10000.00"), "CLP");
                Account savedAccount = accountRepository.save(newAccount);
                transactionRepository.save(new Transaction(null, userId, new BigDecimal("10000.00"), "DEPOSIT"));
                return savedAccount;
            });
    }

    @Transactional(readOnly = true)
    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró una billetera para el usuario con ID: " + userId));
    }

    @Transactional
    public Account deposit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a cero");
        }

        Account account = getAccountByUserId(userId);
        account.setBalance(account.getBalance().add(amount));
        Account updatedAccount = accountRepository.save(account);

        transactionRepository.save(new Transaction(null, userId, amount, "DEPOSIT"));

        return updatedAccount;
    }

    @Transactional
    public void transfer(Long sourceUserId, Long destinationUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a cero");
        }

        if (sourceUserId.equals(destinationUserId)) {
            throw new IllegalArgumentException("No puedes transferirte dinero a ti mismo");
        }

        Account sourceAccount = getAccountByUserId(sourceUserId);
        Account destinationAccount = getAccountByUserId(destinationUserId);

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para completar la transferencia de " + amount + " CLP");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        transactionRepository.save(new Transaction(sourceUserId, destinationUserId, amount, "TRANSFER"));
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findBySourceUserIdOrDestinationUserIdOrderByCreatedAtDesc(userId, userId);
    }
}
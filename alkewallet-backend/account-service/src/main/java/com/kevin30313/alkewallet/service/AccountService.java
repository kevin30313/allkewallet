package com.kevin30313.alkewallet.service;

import com.kevin30313.alkewallet.exception.InsufficientBalanceException;
import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.model.Transaction;
import com.kevin30313.alkewallet.repository.AccountRepository;
import com.kevin30313.alkewallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

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
                Account savedAccount = accountRepository.save(newAccount);

                // Opcional: Registrar el bono de bienvenida como una transacción inicial de tipo DEPOSIT
                transactionRepository.save(new Transaction(null, userId, new BigDecimal("10000.00"), "DEPOSIT"));

                return savedAccount;
            });
    }

    /**
     * Busca la cuenta de un usuario específico.
     */
    @Transactional(readOnly = true)
    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró una billetera para el usuario con ID: " + userId));
    }

    /**
     * Realiza un depósito (Carga de saldo)
     */
    @Transactional
    public Account deposit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a cero");
        }

        Account account = getAccountByUserId(userId);
        account.setBalance(account.getBalance().add(amount));
        Account updatedAccount = accountRepository.save(account);

        // Registrar la transacción de depósito (sourceUserId es null porque viene de afuera)
        transactionRepository.save(new Transaction(null, userId, amount, "DEPOSIT"));
        
        return updatedAccount;
    }

    /**
     * Transferencia de dinero entre dos usuarios de AlkeWallet.
     */
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

        // Modificar saldos
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        // Guardar cambios en las cuentas
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Guardar el registro de la transferencia en el historial
        transactionRepository.save(new Transaction(sourceUserId, destinationUserId, amount, "TRANSFER"));
    }

    /**
     * Obtener el historial completo de transacciones de un usuario (enviadas y recibidas)
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findBySourceUserIdOrDestinationUserIdOrderByCreatedAtDesc(userId, userId);
    }
}
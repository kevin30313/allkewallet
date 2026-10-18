package com.kevin30313.alkewallet;

import com.kevin30313.alkewallet.exception.InsufficientBalanceException;
import com.kevin30313.alkewallet.model.Account;
import com.kevin30313.alkewallet.model.Transaction;
import com.kevin30313.alkewallet.repository.AccountRepository;
import com.kevin30313.alkewallet.repository.TransactionRepository;
import com.kevin30313.alkewallet.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private Account cuentaOrigen;
    private Account cuentaDestino;

    @BeforeEach
    void setUp() {
        cuentaOrigen = new Account();
        cuentaOrigen.setUserId(1L);
        cuentaOrigen.setBalance(new BigDecimal("100000.00"));
        cuentaOrigen.setCurrency("CLP");

        cuentaDestino = new Account();
        cuentaDestino.setUserId(2L);
        cuentaDestino.setBalance(new BigDecimal("20000.00"));
        cuentaDestino.setCurrency("CLP");
    }

    @Nested
    @DisplayName("Pruebas de Búsqueda")
    class ConsultasTests {

        @Test
        @DisplayName("Debe retornar la cuenta cuando el ID de usuario existe")
        void obtenerCuentaPorUserId_Exito() {
            when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(cuentaOrigen));

            Account resultado = accountService.getAccountByUserId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getUserId()).isEqualTo(1L);
            assertThat(resultado.getBalance()).isEqualByComparingTo(new BigDecimal("100000.00"));
            verify(accountRepository, times(1)).findByUserId(1L);
        }
    }

    @Nested
    @DisplayName("Pruebas de Depósitos")
    class DepositosTests {

        @Test
        @DisplayName("Debe incrementar el saldo y registrar transacción al depositar")
        void depositar_Exito() {
            when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(cuentaOrigen));
            when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account cuentaActualizada = accountService.deposit(1L, new BigDecimal("50000.00"));

            assertThat(cuentaActualizada.getBalance()).isEqualByComparingTo(new BigDecimal("150000.00"));
            verify(accountRepository, times(1)).save(cuentaOrigen);
            verify(transactionRepository, times(1)).save(any(Transaction.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si el monto del depósito es cero o negativo")
        void depositar_MontoInvalido() {
            assertThatThrownBy(() -> accountService.deposit(1L, new BigDecimal("0.00")))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(accountRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Pruebas de Transferencias")
    class TransferenciasTests {

        @Test
        @DisplayName("Debe transferir fondos correctamente entre dos cuentas")
        void transferir_Exito() {
            when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(cuentaOrigen));
            when(accountRepository.findByUserId(2L)).thenReturn(Optional.of(cuentaDestino));

            accountService.transfer(1L, 2L, new BigDecimal("30000.00"));

            assertThat(cuentaOrigen.getBalance()).isEqualByComparingTo(new BigDecimal("70000.00"));
            assertThat(cuentaDestino.getBalance()).isEqualByComparingTo(new BigDecimal("50000.00"));

            verify(accountRepository, times(1)).save(cuentaOrigen);
            verify(accountRepository, times(1)).save(cuentaDestino);
            verify(transactionRepository, times(1)).save(any(Transaction.class));
        }

        @Test
        @DisplayName("Debe fallar al transferir si el saldo es insuficiente")
        void transferir_SaldoInsuficiente() {
            when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(cuentaOrigen));
            when(accountRepository.findByUserId(2L)).thenReturn(Optional.of(cuentaDestino));

            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("150000.00")))
                    .isInstanceOf(InsufficientBalanceException.class);

            verify(accountRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }
    }
}

package com.kevin30313.alkewallet.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.kevin30313.alkewallet.exception.AccountServiceException;

import java.util.concurrent.TimeoutException;

@Component
public class AccountServiceClient {

    private final WebClient accountWebClient;

    public AccountServiceClient(WebClient accountWebClient) {
        this.accountWebClient = accountWebClient;
    }

    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackAccountService")
    public void createAccount(Long userId) {
        accountWebClient.post()
            .uri("/api/internal/accounts/create?userId=" + userId)
            .retrieve()
            .toBodilessEntity()
            .block(java.time.Duration.ofSeconds(10));
    }

    public void fallbackAccountService(Long userId, Throwable t) {
        String message = "El servicio de cuentas no está disponible en este momento.";
        
        if (t instanceof CallNotPermittedException) {
            message = "El circuito está abierto: demasiados fallos previos en el servicio de cuentas.";
        } else if (t instanceof TimeoutException || t instanceof java.io.IOException) {
            message = "La comunicación con el servicio de cuentas excedió el tiempo de espera (Timeout).";
        }

        throw new AccountServiceException(message + " Detalle: " + t.getMessage());
    }
}

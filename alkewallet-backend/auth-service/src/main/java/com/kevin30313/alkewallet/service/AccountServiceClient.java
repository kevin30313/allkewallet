package com.kevin30313.alkewallet.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.kevin30313.alkewallet.exception.AccountServiceException;

import java.util.concurrent.TimeoutException;

@Component
public class AccountServiceClient {

    @Autowired
    private WebClient accountWebClient;

    /**
     * Llama al microservicio de cuentas. 
     * Al estar en un componente separado, el proxy de Spring interceptará la llamada
     * y activará el CircuitBreaker correctamente.
     */
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackAccountService")
    public void createAccount(Long userId) {
        accountWebClient.post()
            .uri("/api/internal/accounts/create?userId=" + userId)
            .retrieve()
            .toBodilessEntity()
            .block(java.time.Duration.ofSeconds(10));
    }

    /**
     * Fallback para la creación de cuenta.
     */
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

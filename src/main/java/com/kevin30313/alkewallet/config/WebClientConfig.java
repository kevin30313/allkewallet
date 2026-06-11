package com.kevin30313.alkewallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Configuración dinámica que utiliza una variable de entorno para la URL.
     * Si no se define la variable, utiliza localhost por defecto para desarrollo local.
     */
    @Bean
    public WebClient accountWebClient(WebClient.Builder builder) {
        String baseUrl = System.getenv("ACCOUNT_SERVICE_URL");
        
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://127.0.0.1:8081";
        }
        
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
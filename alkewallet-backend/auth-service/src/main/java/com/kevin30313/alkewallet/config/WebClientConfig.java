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
     * Forzamos directamente la IP 127.0.0.1 para que no intente resolver "localhost"
     */
    @Bean
    public WebClient accountWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://127.0.0.1:8081")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
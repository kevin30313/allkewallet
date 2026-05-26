package com.kevin30313.alkewallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitamos CSRF usando la sintaxis recomendada para Spring Boot 3.x
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas internas: paso libre temporal para comunicación entre microservicios
                .requestMatchers("/api/accounts/internal/**").permitAll()
                
                // 2. Rutas del cliente (Como ver saldo): Requieren autenticación estricta
                .requestMatchers("/api/accounts/user/**").authenticated()
                
                // 3. Cualquier otra petición debe estar firmada
                .anyRequest().authenticated()
            );
            

        return http.build();
    }
}
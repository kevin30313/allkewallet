package com.kevin30313.alkewallet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para pruebas iniciales
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/register", "/login").permitAll() // Rutas públicas
                .anyRequest().permitAll() // Todo lo demás requiere login
            )
            .formLogin(form -> form.disable()) // Mantiene el formulario por si lo necesitas
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
	



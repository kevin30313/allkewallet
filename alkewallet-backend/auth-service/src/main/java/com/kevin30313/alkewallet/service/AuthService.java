package com.kevin30313.alkewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.kevin30313.alkewallet.dto.LoginRequest;
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.model.User;
import com.kevin30313.alkewallet.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // 1. Guardamos el usuario en 'alkewallet_auth_db'
        User savedUser = userRepository.save(user);

        // 2. ¡EL PUENTE! Llamada sincrónica para crear la billetera en el account-service
        try {
            webClientBuilder.build()
                .post()
                // Apuntamos al puerto local 8081 que usará el servicio de cuentas
                .uri("http://localhost:8081/api/accounts/internal/create?userId=" + savedUser.getId())
                .retrieve()
                .toBodilessEntity()
                .block(); // .block() congela el hilo un milisegundo esperando la respuesta del otro microservicio
        } catch (Exception e) {
            // Si el servicio de cuentas falla o está caído, lanzamos excepción para hacer rollback si fuera necesario
            throw new RuntimeException("Error crítico: No se pudo inicializar la billetera digital del usuario. " + e.getMessage());
        }

        return savedUser;
    }

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().trim(), 
                request.getPassword().trim()
            )
        );

        // Generamos el token usando el nombre (email) autenticado
        return jwtService.generateToken(authentication.getName());
    }

    @Transactional(readOnly = true)
    public User getUserProfile(String token) {
        // Asegúrate de que en JwtService.java el método se llame extractUsername
        String email = jwtService.extractUsername(token); 
        
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
package com.kevin30313.alkewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Es buena idea setear un saldo inicial si la entidad lo permite
        // user.setSaldo(0.0); 
        return userRepository.save(user);
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
    public User getUserProfile(String token) {
    // Cambiamos jwtUtil por jwtService
    // Asegúrate de que en JwtService.java el método se llame extractUsername o extractEmail
    String email = jwtService.extractUsername(token); 
    
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
}
}
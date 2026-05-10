package com.kevin30313.alkewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kevin30313.alkewallet.dto.LoginRequest; 
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.service.AuthService;

import java.util.Collections;
import java.util.Map;

@CrossOrigin(origins = "https://alkewallet-frontend.onrender.com") 
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Es buena práctica devolver 201 Created para registros exitosos
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("DEBUG: Intento de login para: " + request.getEmail());
            String token = authService.login(request);
            
            // IMPORTANTE: Devolvemos un JSON { "token": "valor..." } 
            // Esto evita problemas de parseo en el Frontend
            return ResponseEntity.ok(Collections.singletonMap("token", token));
            
        } catch (Exception e) {
            e.printStackTrace(); 
            // Enviamos un JSON incluso en el error para mantener la consistencia
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("error", "Credenciales inválidas o error de servidor"));
        }
    }
}
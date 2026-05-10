package com.kevin30313.alkewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kevin30313.alkewallet.dto.LoginRequest; 
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.service.AuthService;

@CrossOrigin(origins = "https://alkewallet-frontend.onrender.com") 
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
     try {
        System.out.println("DEBUG: Intento de login para: " + request.getEmail());
        String response = authService.login(request);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        // ESTO ES LO MÁS IMPORTANTE: Imprimir el error real en la consola de Render
        e.printStackTrace(); 
        return ResponseEntity.status(401).body("Error real: " + e.getMessage());
        }
    }
}
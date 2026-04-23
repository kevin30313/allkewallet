package com.kevin30313.alkewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.service.AuthService;

@RestController
@RequestMapping("/api/auth") // Esta es la ruta base
public class AuthController {

    @Autowired
    private AuthService authService;

    // Este método maneja el POST a /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

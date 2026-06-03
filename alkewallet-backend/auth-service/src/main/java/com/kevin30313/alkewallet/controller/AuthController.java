package com.kevin30313.alkewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin30313.alkewallet.dto.LoginRequest;
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.dto.UserResponseDTO;
import com.kevin30313.alkewallet.model.User;
import com.kevin30313.alkewallet.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario en la plataforma.
     * Modificado temporalmente para interceptar errores de transacción en consola y Postman.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserResponseDTO response = authService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("\n==================================================");
            System.out.println("❌ ERROR DETECTADO EN EL PROCESO DE REGISTRO:");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("==================================================\n");
            e.printStackTrace(); // Esto pintará el causante real arriba del stack trace anterior
            
            // Le devolvemos el error a Postman para romper el velo del 403
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en backend: " + e.getMessage());
        }
    }

    /**
     * Endpoint para iniciar sesión.
     * Modificado temporalmente para interceptar errores ocultos de autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            System.out.println("\n==================================================");
            System.out.println("❌ ERROR DETECTADO EN EL PROCESO DE LOGIN:");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("==================================================\n");
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error en login: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener el perfil del usuario autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User userProfile = authService.getUserProfile(token);
        return ResponseEntity.ok(userProfile);
    }
}
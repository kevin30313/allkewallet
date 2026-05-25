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
     * Recibe los datos en un RegisterRequest y responde con un DTO seguro (UserResponseDTO).
     * Retorna HTTP 201 Created si la persistencia y el account-service fueron exitosos.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequest request) {
        // El servicio maneja la lógica interna y la comunicación sincrónica por red
        UserResponseDTO response = authService.register(request);
        
        // Estándar REST: Al crear un recurso con éxito se responde con un estado 201
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint para iniciar sesión.
     * Autentica las credenciales y devuelve el token JWT generado para el cliente.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    /**
     * Endpoint para obtener el perfil del usuario autenticado.
     * Lee el token enviado en la cabecera 'Authorization' para extraer los datos seguros.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
        // Si el token viene con el prefijo "Bearer ", se lo removemos para procesarlo limpio
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        User userProfile = authService.getUserProfile(token);
        return ResponseEntity.ok(userProfile);
    }
}
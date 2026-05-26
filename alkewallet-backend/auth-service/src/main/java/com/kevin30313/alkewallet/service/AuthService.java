package com.kevin30313.alkewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.kevin30313.alkewallet.dto.LoginRequest;
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.dto.UserResponseDTO;
import com.kevin30313.alkewallet.exception.AccountServiceException;
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

    // 1. Inyectamos la URL desde application.properties (Inversión de Dependencias)
    @Value("${app.services.account.url}")
    private String accountServiceUrl;

    /**
     * Registra un usuario y retorna un DTO seguro sin exponer datos sensibles.
     */
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Persistencia local
        User savedUser = userRepository.save(user);

        // 2. Comunicación remota usando la variable inyectada
        try {
            webClientBuilder.build()
                .post()
                .uri(accountServiceUrl + "/api/internal/accounts/create?userId=" + savedUser.getId())
                .retrieve()
                .toBodilessEntity()
                .block(); 
                
        } catch (Exception e) {
            // 3. Lanzamos nuestra excepción personalizada para activar el Rollback
            throw new AccountServiceException("No se pudo inicializar la billetera en account-service: " + e.getMessage());
        }

        // 4. Mapeo a DTO de salida (Nunca retornamos la entidad pura al controlador)
        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().trim(), 
                request.getPassword().trim()
            )
        );
        return jwtService.generateToken(authentication.getName());
    }

    @Transactional(readOnly = true)
    public User getUserProfile(String token) {
        String email = jwtService.extractUsername(token); 
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
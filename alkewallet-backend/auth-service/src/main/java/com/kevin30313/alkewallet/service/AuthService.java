package com.kevin30313.alkewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private WebClient accountWebClient;

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);

        try {
            accountWebClient.post()
                .uri("/api/internal/accounts/create?userId=" + savedUser.getId())
                .retrieve()
                .toBodilessEntity()
                .block(); 
                
        } catch (Exception e) {
            throw new AccountServiceException("No se pudo inicializar la billetera en account-service: " + e.getMessage());
        }

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
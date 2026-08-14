package com.kevin30313.alkewallet.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kevin30313.alkewallet.dto.LoginRequest;
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.dto.UserResponseDTO;
import com.kevin30313.alkewallet.model.User;
import com.kevin30313.alkewallet.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccountServiceClient accountServiceClient;

    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       AuthenticationManager authenticationManager, 
                       JwtService jwtService, 
                       AccountServiceClient accountServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.accountServiceClient = accountServiceClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO register(RegisterRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String email = request.getEmail() != null ? request.getEmail().trim() : null;

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario '" + username + "' ya está en uso.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El correo '" + email + "' ya está registrado.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        accountServiceClient.createAccount(savedUser.getId());

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().trim(), 
                request.getPassword()
            )
        );
        User user = userRepository.findByEmail(request.getEmail().trim())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return jwtService.generateToken(authentication.getName(), user.getId());
    }

    @Transactional(readOnly = true)
    public User getUserProfile(String token) {
        String email = jwtService.extractUsername(token); 
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
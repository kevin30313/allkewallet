package com.kevin30313.alkewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO register(RegisterRequest request) {
        // 1. Validaciones previas con trim() para evitar duplicados por espacios
        String trimmedUsername = request.getUsername() != null ? request.getUsername().trim() : null;
        String trimmedEmail = request.getEmail() != null ? request.getEmail().trim() : null;

        if (userRepository.findByUsername(trimmedUsername).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario '" + trimmedUsername + "' ya está en uso.");
        }
        if (userRepository.findByEmail(trimmedEmail).isPresent()) {
            throw new IllegalArgumentException("El correo '" + trimmedEmail + "' ya está registrado.");
        }

        // 2. Preparar el objeto User.
        User user = new User();
        user.setUsername(trimmedUsername);
        user.setEmail(trimmedEmail);
        // NO trim() en password para permitir espacios si el usuario lo desea, 
        // pero debe ser consistente con el login.
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // 3. Persistencia en base de datos.
        User savedUser = userRepository.save(user);

        // 4. Llamada al microservicio de cuentas (delegada a AccountServiceClient para que funcione el AOP/CircuitBreaker)
        accountServiceClient.createAccount(savedUser.getId());

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public String login(LoginRequest request) {
        // Trim en email es estándar, pero en password es arriesgado si no se hizo en register.
        // Lo removemos del password para ser consistentes con register (donde no se hizo trim).
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().trim(), 
                request.getPassword()
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
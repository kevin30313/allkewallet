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

	    public User register(RegisterRequest request) {
	        User user = new User();
	        user.setUsername(request.getUsername());
	        user.setEmail(request.getEmail());
	        
	        // 
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	        
	        return userRepository.save(user);
	        }
	        /**
	         * Valida las credenciales del usuario.
	         * Si son correctas, devuelve un mensaje de éxito.
	         */
	        public String login(LoginRequest request) {
	            // El AuthenticationManager se encarga de comparar el username 
	            // y la password (desencriptando internamente)
	            Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                    request.getUsername(), 
	                    request.getPassword()
	                )
	            );

	            // Si llega a este punto, la autenticación fue exitosa.
	            return "Login exitoso para el usuario: " + authentication.getName();
	        
	    }
	}


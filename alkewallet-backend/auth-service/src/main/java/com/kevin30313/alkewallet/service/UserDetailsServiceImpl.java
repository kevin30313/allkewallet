package com.kevin30313.alkewallet.service;

import com.kevin30313.alkewallet.repository.UserRepository;
import org.springframework.security.core.userdetails.User; // Importante: usar el User de Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos al usuario en la DB. Usamos .trim() por si acaso hay espacios en el input.
        com.kevin30313.alkewallet.model.User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // 2. Construimos y retornamos el User oficial de Spring Security.
        // No usamos .trim() en la contraseña de la DB a menos que sea estrictamente necesario (ej. columna CHAR)
        return User.builder()
                .username(user.getEmail().trim())
                .password(user.getPassword())
                .authorities(new ArrayList<>()) // Aquí puedes mapear roles en el futuro
                .build();
    }
}
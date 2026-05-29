package com.kevin30313.alkewallet.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Cambia este texto por la clave secreta exacta de tu auth-service
//  Código corregido y limpio
    private final String SECRET_KEY = "EstaEsMiLlaveSecretaSuperLargaYSeguraParaAlkeWallet2026";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Extraemos la cabecera "Authorization" de la petición HTTP
        String authHeader = request.getHeader("Authorization");

        // 2. Validamos que la cabecera exista y empiece con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extraemos solo el JWT (quitando los primeros 7 caracteres)
            
            try {
                // 3. Parseamos y validamos la firma del token con nuestra clave secreta
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                if (username != null) {
                    // 4. Si el token es real y el usuario es válido, creamos el objeto de autenticación
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    
                    // 5. Lo guardamos en el contexto de Spring para que apruebe la petición
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Si el token falló por expiración o firma corrupta, nos aseguramos de limpiar el contexto
                SecurityContextHolder.clearContext();
            }
        }

        // 6. Pasamos el control al siguiente filtro en la cadena de Spring Boot
        filterChain.doFilter(request, response);
    }
}
package com.kevin30313.alkewallet.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct; 
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    
    @Value("${JWT_SECRET}")
    private String secretKeyString;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // Convierte tu texto de configuración en una llave válida para el algoritmo HS256
        byte[] keyBytes = secretKeyString.getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 1. Generar el Token JWT para un usuario
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 2. Extraer el nombre de usuario (Subject) del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 3. Validar si el token es legítimo y no ha expirado
     */
    public boolean isTokenValid(String token, String userDetailsUsername) {
        final String username = extractUsername(token);
        return (username.equals(userDetailsUsername)) && !isTokenExpired(token);
    }

    // --- MÉTODOS DE APOYO ---

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
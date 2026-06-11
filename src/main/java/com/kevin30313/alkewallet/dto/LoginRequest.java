package com.kevin30313.alkewallet.dto;

public class LoginRequest {
    private String email; // Cambiado de username a email para coincidir con la lógica
    private String password;

    // Getters y Setters corregidos
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
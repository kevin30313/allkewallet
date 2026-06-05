package com.kevin30313.alkewallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_user_id")
    private Long sourceUserId; // Quién envía (será null en caso de depósitos de bienvenida o cargas externas)

    @Column(name = "destination_user_id", nullable = false)
    private Long destinationUserId; // Quién recibe el dinero

    @Column(nullable = false)
    private BigDecimal amount; // Monto de la operación

    @Column(nullable = false)
    private String type; // "TRANSFER", "DEPOSIT", "WITHDRAWAL"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructor vacío obligatorio para JPA
    public Transaction() {
    }

    // Constructor rápido para registrar movimientos de forma limpia
    public Transaction(Long sourceUserId, Long destinationUserId, BigDecimal amount, String type) {
        this.sourceUserId = sourceUserId;
        this.destinationUserId = destinationUserId;
        this.amount = amount;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSourceUserId() { return sourceUserId; }
    public void setSourceUserId(Long sourceUserId) { this.sourceUserId = sourceUserId; }

    public Long getDestinationUserId() { return destinationUserId; }
    public void setDestinationUserId(Long destinationUserId) { this.destinationUserId = destinationUserId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
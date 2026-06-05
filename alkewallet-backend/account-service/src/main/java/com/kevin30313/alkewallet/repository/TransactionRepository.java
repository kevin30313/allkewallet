package com.kevin30313.alkewallet.repository;

import com.kevin30313.alkewallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Trae el historial de movimientos de un usuario (ya sea que haya enviado o recibido el dinero) ordenado por fecha
    List<Transaction> findBySourceUserIdOrDestinationUserIdOrderByCreatedAtDesc(Long sourceUserId, Long destinationUserId);
}
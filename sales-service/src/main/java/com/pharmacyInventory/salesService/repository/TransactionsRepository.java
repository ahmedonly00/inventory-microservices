package com.pharmacyInventory.salesService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.salesService.model.Transactions;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByMedicationId(Long medicationId);
    boolean existsByMedicationId(Long medicationId);
    List<Transactions> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
}


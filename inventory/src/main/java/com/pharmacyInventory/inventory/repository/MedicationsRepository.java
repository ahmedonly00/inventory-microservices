package com.pharmacyInventory.inventory.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Medications;

@Repository
public interface MedicationsRepository extends JpaRepository<Medications, Long> {
    List<Medications> findByNameContainingIgnoreCase(String name);
    List<Medications> findByIsActive(Boolean isActive);
    List<Medications> findByStockQuantityLessThan(Integer stockQuantity);
    List<Medications> findByReorderLevelLessThan(Integer reorderLevel);
    List<Medications> findByExpiryDateLessThan(LocalDate expiryDate);
    List<Medications> findByBatchNumberContainingIgnoreCase(String batchNumber);
    List<Medications> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByName(String name);

    // Alert-specific queries
    @Query("SELECT m FROM Medications m WHERE m.stockQuantity <= m.reorderLevel AND m.isActive = true")
    List<Medications> findLowStockMedications();
    
    @Query("SELECT m FROM Medications m WHERE m.stockQuantity = 0 AND m.isActive = true")
    List<Medications> findOutOfStockMedications();
    
    @Query("SELECT m FROM Medications m WHERE m.expiryDate BETWEEN :startDate AND :endDate AND m.isActive = true")
    List<Medications> findExpiringMedications(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT m FROM Medications m WHERE m.stockQuantity < m.reorderLevel AND m.isActive = true")
    List<Medications> findCriticalLowStockMedications();
}

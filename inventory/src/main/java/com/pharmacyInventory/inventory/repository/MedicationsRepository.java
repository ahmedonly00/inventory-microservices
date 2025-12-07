package com.pharmacyInventory.inventory.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.Enum.StockStatus;

@Repository
public interface MedicationsRepository extends JpaRepository<Medications, Long> {
    List<Medications> findByNameContainingIgnoreCase(String name);
    List<Medications> findByStockStatusAndBranchId(StockStatus stockStatus, String branchId);
    List<Medications> findByStockQuantityLessThan(Integer stockQuantity);
    List<Medications> findByReorderLevelLessThan(Integer reorderLevel);
    List<Medications> findByExpiryDateLessThan(LocalDate expiryDate);
    List<Medications> findByBatchNumberContainingIgnoreCase(String batchNumber);
    List<Medications> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByName(String name);
    List<Medications> findAllByBranchId(String branchId );
    Optional<Medications> findByMedicationIdAndBranchId(Long medicationId, String branchId);
    List<Medications> findByNameContainingIgnoreCaseAndBranchId(String name, String branchId);
    List<Medications> findByCategoryIdAndBranchId(Long categoryId, String branchId);



    // Alert-specific queries
    @Query("SELECT m FROM Medications m WHERE m.stockQuantity <= m.reorderLevel AND m.stockStatus = StockStatus.LOW_STOCK")
    List<Medications> findLowStockMedications();
    
    @Query("SELECT m FROM Medications m WHERE m.stockQuantity = 0 AND m.stockStatus = StockStatus.OUT_OF_STOCK")
    List<Medications> findOutOfStockMedications();
    
    @Query("SELECT m FROM Medications m WHERE m.expiryDate BETWEEN :startDate AND :endDate AND m.stockStatus = StockStatus.IN_STOCK")
    List<Medications> findExpiringMedications(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT m FROM Medications m WHERE m.stockQuantity < m.reorderLevel AND m.stockStatus = StockStatus.IN_STOCK")
    List<Medications> findCriticalLowStockMedications();
}

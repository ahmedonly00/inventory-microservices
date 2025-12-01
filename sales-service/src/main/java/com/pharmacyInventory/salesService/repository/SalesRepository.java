package com.pharmacyInventory.salesService.repository;

import com.pharmacyInventory.salesService.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
    List<Sales> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Sales> findByUserId(Long userId);
    
    List<Sales> findByPaymentMethod(String paymentMethod);
}

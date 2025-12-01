package com.pharmacyInventory.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
}

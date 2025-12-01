package com.pharmacyInventory.inventory.repository;

import com.pharmacyInventory.inventory.model.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {
    
    boolean existsByName(String name);
    
    boolean existsByEmail(String email);
}

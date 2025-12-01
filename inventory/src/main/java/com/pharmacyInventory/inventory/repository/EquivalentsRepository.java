package com.pharmacyInventory.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Equivalents;
import com.pharmacyInventory.inventory.model.Medications;

@Repository
public interface EquivalentsRepository extends JpaRepository<Equivalents, Long> {
    List<Equivalents> findByOriginalMedication(Medications originalMedication);
    List<Equivalents> findByEquivalentMedication(Medications equivalentMedication);
    
}

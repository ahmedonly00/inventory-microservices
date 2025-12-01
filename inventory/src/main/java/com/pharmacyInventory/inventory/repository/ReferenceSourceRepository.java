package com.pharmacyInventory.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.ReferenceSource;

@Repository
public interface ReferenceSourceRepository extends JpaRepository<ReferenceSource, Long> {
    Optional<ReferenceSource> findByName(String name);
    boolean existsByName(String name);
}

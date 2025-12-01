package com.pharmacyInventory.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Brands;


@Repository
public interface BrandRepository extends JpaRepository<Brands, Long>{
    Optional<Brands> findByName(String name);
    
}

package com.pharmacyInventory.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Brands;



@Repository
public interface BrandRepository extends JpaRepository<Brands, Long>{
    Optional<Brands> findByName(String name);

    @Query("SELECT COUNT(b) FROM Brands b WHERE b.branchId = :branchId")
    long countByBranchId(@Param("branchId") String branchId);
        
}

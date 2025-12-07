package com.pharmacyInventory.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.Enum.CategoryType;
import com.pharmacyInventory.inventory.model.Categories;


@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    
    Optional<Categories> findByName(String name);
    
    boolean existsByName(String name);

    Optional<Categories> findByIdAndBranchId(Long id, String branchId);

    List<Categories> findByBranchId(String branchId);

    @Query("SELECT c FROM Categories c WHERE c.type = :type AND c.branchId = :branchId")
    List<Categories> findByType(
        @Param("type") CategoryType type,
        @Param("branchId") String branchId
    );
 
}

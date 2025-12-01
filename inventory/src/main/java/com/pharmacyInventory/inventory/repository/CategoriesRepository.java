package com.pharmacyInventory.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.Enum.CategoryType;
import com.pharmacyInventory.inventory.model.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    
    Optional<Categories> findByName(String name);
    
    boolean existsByName(String name);

    List<Categories> findByType(CategoryType type);
 
}

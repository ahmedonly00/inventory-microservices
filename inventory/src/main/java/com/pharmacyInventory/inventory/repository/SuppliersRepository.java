package com.pharmacyInventory.inventory.repository;

import com.pharmacyInventory.inventory.model.Suppliers;

import java.util.List;
import java.util.Optional;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {
    
    boolean existsByName(String name);
    
    boolean existsByEmail(String email);

    List<Suppliers> findByBranchId(String branchId);
    
    Page<Suppliers> findByBranchId(String branchId, Pageable pageable);
    
    Optional<Suppliers> findByIdAndBranchId(Long id, String branchId);

    Optional<Suppliers> findByIsActiveTrueAndBranchId(String branchId);

    Optional<Suppliers> findByIsActiveFalseAndBranchId(String branchId);
        
    boolean existsByEmailAndBranchId(String email, String branchId);

    List<Suppliers> searchByNameOrEmail(String query, String branchId);
    
    boolean existsByPhoneAndBranchId(String phoneNumber, String branchId);
        
    void deleteByIdAndBranchId(Long id, String branchId);
    
    boolean existsByIdAndBranchId(Long id, String branchId);
    
    @Query("SELECT s FROM Suppliers s WHERE s.branchId = :branchId AND " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Suppliers> findByBranchIdAndNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            @Param("branchId") String branchId,
            @Param("query") String nameQuery,
            @Param("query") String emailQuery);
}

package com.pharmacyInventory.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Equivalents;
import com.pharmacyInventory.inventory.model.Medications;

@Repository
public interface EquivalentsRepository extends JpaRepository<Equivalents, Long> {
    List<Equivalents> findByOriginalMedication(Medications originalMedication);
    List<Equivalents> findByEquivalentMedication(Medications equivalentMedication);
    List<Equivalents> findByInnAndBrandNamesContaining(String inn, String brandNames);
    List<Equivalents> findByFormAndReferenceSourceName(String form, String referenceSource);
    List<Equivalents> findByBranchId(String branchId);
    Page<Equivalents> findByBranchId(String branchId, Pageable pageable);
    Optional<Equivalents> findByIdAndBranchId(Long id, String branchId);
    void deleteByIdAndBranchId(Long id, String branchId);
    boolean existsByIdAndBranchId(Long id, String branchId);

    @Query("SELECT COUNT(DISTINCT e.inn) FROM Equivalents e WHERE e.branchId = :branchId")
    long countDistinctInnByBranchId(String branchId);

    @Query("SELECT e.form, COUNT(e) FROM Equivalents e WHERE e.branchId = :branchId GROUP BY e.form")
    List<Object[]> countByFormAndBranchId(String branchId);

    @Query("SELECT e.referenceSourceName, COUNT(e) FROM Equivalents e WHERE e.branchId = :branchId GROUP BY e.referenceSourceName")
    List<Object[]> countByReferenceSourceAndBranchId(String branchId);

    @Query("SELECT e.inn, COUNT(e) as count FROM Equivalents e WHERE e.branchId = :branchId GROUP BY e.inn ORDER BY count DESC")
    List<Object[]> findTopInnsByBranchId(@Param("branchId") String branchId, Pageable pageable);

    @Query("SELECT e FROM Equivalents e WHERE e.branchId = :branchId AND " +
        "(LOWER(e.inn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(e.brandNames) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Equivalents> findByInnContainingOrBrandNameContaining(
        @Param("query") String query, 
        @Param("branchId") String branchId
    );
    

    @Query("SELECT COUNT(e) FROM Equivalents e WHERE e.branchId = :branchId")
    long countByBranchId(@Param("branchId") String branchId);


    
}

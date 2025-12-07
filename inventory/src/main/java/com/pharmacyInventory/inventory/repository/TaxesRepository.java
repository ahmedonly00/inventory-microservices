package com.pharmacyInventory.inventory.repository;

import com.pharmacyInventory.inventory.model.Taxes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxesRepository extends JpaRepository<Taxes, Long> {
    List<Taxes> findByIsActive(@NonNull Boolean isActive);
    Optional<Taxes> findByName(@NonNull String name);
    List<Taxes> findAllByBranchId(@NonNull String branchId);
    Optional<Taxes> findByIdAndBranchId(@NonNull Long id, @NonNull String branchId);
    boolean existsByIdAndBranchId(@NonNull Long id, @NonNull String branchId);
    List<Taxes> findByIsActiveAndBranchId(@NonNull Boolean isActive, @NonNull String branchId);
}

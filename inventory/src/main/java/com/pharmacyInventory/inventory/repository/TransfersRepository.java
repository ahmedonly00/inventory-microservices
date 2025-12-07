package com.pharmacyInventory.inventory.repository;

import com.pharmacyInventory.inventory.model.Transfers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfersRepository extends JpaRepository<Transfers, Long>{

    List<Transfers> findByBranchIdOrToBranchId(String branchId, String toBranchId);
    List<Transfers> findAllByBranchId(String branchId);
    List<Transfers> findByIdOrBranchId(Long id, String branchId);
    Page<Transfers> findByBranchId(String branchId, Pageable pageable);


}

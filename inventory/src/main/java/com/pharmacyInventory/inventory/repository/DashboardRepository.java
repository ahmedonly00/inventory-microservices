package com.pharmacyInventory.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacyInventory.inventory.model.Dashboard;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    
}

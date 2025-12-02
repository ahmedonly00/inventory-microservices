package com.pharmacyInventory.inventory.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id",nullable = false)
    private Long id;

    @Column(name = "from_branch_id",nullable = false)
    private Long fromBranchId;

    @Column(name = "to_branch_id",nullable = false)
    private Long toBranchId;
    
    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;

    @Column(name = "received_date", nullable = false)
    private LocalDateTime receivedDate;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "from_location")
    private String fromLocation;
    
    @Column(name = "to_location")
    private String toLocation;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medications medication;

    @PrePersist
    protected void onCreate() {
        transferDate = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        receivedDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
 
}

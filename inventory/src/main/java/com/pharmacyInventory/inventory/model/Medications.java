package com.pharmacyInventory.inventory.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pharmacyInventory.inventory.Enum.StockStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicationId;

    @Column(nullable = false)
    private String branchId;
    
    @Column(nullable = false)
    private String name;

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Categories form;

    @Column(name = "strength", nullable = false)
    private String strength;

    @Column(name = "stock_quantity",nullable = false)
    private Integer stockQuantity;

    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel;

    @Column(nullable = false)
    private Float price;
    
    @Column(name = "batch_number", nullable = false)
    private String batchNumber;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "stock_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StockStatus stockStatus = StockStatus.IN_STOCK;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //relationships
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Suppliers suppliers;
    
    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL)
    private List<Transfers> transfers;
    
    @OneToMany(mappedBy = "originalMedication", cascade = CascadeType.ALL)
    private List<Equivalents> equivalents;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL)
    private List<Stock> stock;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
}

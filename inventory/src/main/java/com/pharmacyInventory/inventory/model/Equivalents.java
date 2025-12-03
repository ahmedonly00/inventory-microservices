package com.pharmacyInventory.inventory.model;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "equivalents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equivalents {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String branchId;

    @Column(nullable = false)
    private String inn;

    @Column(nullable = false)
    private String form;

    @Column(nullable = false)
    private String strength;

    @Column(nullable = false)
    private String referenceSourceName;

    @Column(nullable = false)
    private List<Long> brandIds;

    @Column(nullable = false)
    private List<String> brandNames;

    @Column(nullable = false)
    private String source;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medications originalMedication;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equivalent_medication_id", nullable = false)
    private Medications equivalentMedication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_source_id", nullable = false)
    private ReferenceSource referenceSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brands brand;

    @PrePersist
    protected void onCreate() { 
        this.createdAt = LocalDateTime.now();
    }
    
}

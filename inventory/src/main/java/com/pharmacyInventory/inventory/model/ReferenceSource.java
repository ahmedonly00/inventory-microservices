package com.pharmacyInventory.inventory.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "reference_sources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenceSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;

    //relationship
    @OneToMany(mappedBy = "referenceSource", cascade = CascadeType.ALL)
    private List<Equivalents> equivalents;
    
}

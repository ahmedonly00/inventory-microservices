package com.pharmacyInventory.salesService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;
    
    @Column(name = "sub_total", nullable = false)
    private Double subTotal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medications medication;
    
    @PrePersist
    protected void calculateSubTotal() {
        if (this.unitPrice != null && this.quantity != null) {
            this.subTotal = this.unitPrice * this.quantity;
        }
    }
}

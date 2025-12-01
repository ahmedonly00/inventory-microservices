package com.pharmacyInventory.inventory.dtos.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    private Long id;
    private Integer quantity;
    private String batchNumber;
    private String expiryDate;
    private Long medicationId;
    private String medicationName;
}

package com.pharmacyInventory.salesService.dtos.sales;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemDTO {
    private Long id;
    private Integer quantity;
    private Integer unitPrice;
    private Integer subTotal;
    private Long saleId;
    private Long medicationId;
    private String medicationName;
}

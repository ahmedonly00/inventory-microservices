package com.pharmacyInventory.inventory.dtos.Brands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandsDTO {
    private Long id;
    private String branchId;
    private String name;
}

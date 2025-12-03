package com.pharmacyInventory.inventory.dtos.ReferenceSource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferenceSourceDTO {
    private Long id;
    private String branchId;
    private String name;
}

package com.pharmacyInventory.inventory.dtos.taxes;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxesDTO {
    private Long id;
    private String branchId;
    private String name;
    private Float rate;
    private String type;
    private Boolean isActive;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> salesIds;
}

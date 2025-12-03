package com.pharmacyInventory.inventory.dtos.equivalents;

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
public class EquivalentsDTO {
    private Long id;
    private String branchId;
    private String inn;
    private String form;
    private String strength;
    private Long referenceSourceId;
    private String referenceSourceName;
    private List<Long> brandIds;
    private List<String> brandNames;
    private LocalDateTime createdAt;
    private Long originalMedicationId;
    private String originalMedicationName;
    private Long equivalentMedicationId;
    private String equivalentMedicationName;
}

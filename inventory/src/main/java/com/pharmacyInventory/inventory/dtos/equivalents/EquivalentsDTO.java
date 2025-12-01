package com.pharmacyInventory.inventory.dtos.equivalents;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquivalentsDTO {
    private Long id;
    private String inn;
    private String form;
    private String strength;
    private String brands;
    private String source;
    private LocalDateTime createdAt;
    private Long originalMedicationId;
    private String originalMedicationName;
    private Long equivalentMedicationId;
    private String equivalentMedicationName;
}

package com.pharmacyInventory.inventory.dtos.medications;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationsDTO {
    private Long medicationId;
    private String name;
    private String form;
    private String strength;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private Float price;
    private String batchNumber;
    private LocalDate expiryDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long addedById;
    private String addedByName;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    private String categoryName;
    
    @NotNull(message = "Form ID is required")
    private Long formId;
    
    private String formName;
    
    private Long supplierId;
    private String supplierName;
}

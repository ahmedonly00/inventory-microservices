package com.pharmacyInventory.inventory.dtos.suppliers;

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
public class SuppliersDTO {
    private Long id;
    private String name;
    private String contactName;
    private String email;
    private String phone;
    private String address;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private List<Long> medicationIds;
}

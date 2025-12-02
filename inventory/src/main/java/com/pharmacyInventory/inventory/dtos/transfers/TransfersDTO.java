package com.pharmacyInventory.inventory.dtos.transfers;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransfersDTO {
    private Long id;
    private Long fromBranchId;
    private Long toBranchId;
    private Integer quantity;
    private LocalDateTime transferDate;
    private LocalDateTime receivedDate;
    private String status;
    private String fromLocation;
    private String toLocation;
    private String notes;
    private Long medicationId;
    private String medicationName;
    private Long requestedById;
    private String requestedByName;
}

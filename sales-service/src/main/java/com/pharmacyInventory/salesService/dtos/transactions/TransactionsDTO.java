package com.pharmacyInventory.salesService.dtos.transactions;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsDTO {
    private Long id;
    private String type;
    private Integer quantity;
    private Float amount;
    private String reference;
    private String notes;
    private String transactionType;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long medicationId;
    private String medicationName;
    private Long supplierId;
    private String supplierName;
    private Long userId;
    private String userName;
}

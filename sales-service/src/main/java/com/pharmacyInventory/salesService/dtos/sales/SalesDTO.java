package com.pharmacyInventory.salesService.dtos.sales;

import com.pharmacyInventory.Enum.PaymentMethod;
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
public class SalesDTO {
    private Long id;
    private Integer totalAmount;
    private Integer taxAmount;
    private PaymentMethod paymentMethod;
    private Integer discount;
    private LocalDateTime saleDate;
    private String customerName;
    private String customerPhone;
    private Long userId;
    private String userName;
    private List<Long> saleItemIds;
    private List<Long> taxIds;
}

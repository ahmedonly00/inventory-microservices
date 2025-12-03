package com.pharmacyInventory.inventory.feign;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pharmacyInventory.inventory.config.FeignConfig;

@FeignClient(name = "sales-service", configuration = FeignConfig.class)
public interface SalesServiceClient {

    @GetMapping("/api/sales/total-amount")
    Double getTotalSales();
    
    @GetMapping("/api/sales/total-amount-by-date")
    Double getTotalSalesByDateRange(
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    );
    
    @GetMapping("/api/sales/payment-methods")
    Map<String, Double> getPaymentMethodBreakdown();

    @GetMapping("/api/sales/top-selling")
    List<Map<String, Object>> getTopSellingMedications(@RequestParam("limit") int limit);
    
}

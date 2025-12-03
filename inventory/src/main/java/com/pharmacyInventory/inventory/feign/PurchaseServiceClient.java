package com.pharmacyInventory.inventory.feign;

import java.time.LocalDateTime;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pharmacyInventory.inventory.config.FeignConfig;

@FeignClient(name = "sales-service", configuration = FeignConfig.class)
public interface PurchaseServiceClient {

    @GetMapping("/api/purchases/total-amount")
    Double getTotalPurchases();
    
    @GetMapping("/api/purchases/total-amount-by-date")
    Double getTotalPurchasesByDateRange(
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    );
    
}

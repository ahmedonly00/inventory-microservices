package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.pharmacyInventory.inventory.config.FeignConfig;

@FeignClient(name = "dashboard-service", configuration = FeignConfig.class)
public interface DashboardServiceClient {
    
    
}

package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "dashboard-service", url = "${dashboard.service.url}")
public interface DashboardServiceClient {
    
    
}


package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "bulk-operations-service", 
             url = "${bulk.operations.service.url}",
             fallback = BulkOperationsServiceFallback.class)
public interface BulkOperationsServiceClient {
    
    @PostMapping("/api/bulk/medications/delete")
    Map<String, Object> bulkDeleteMedications(@RequestBody List<Long> medicationIds);

    @PostMapping("/api/bulk/medications/update-prices")
    Map<String, Object> bulkUpdatePrices(@RequestBody Map<String, Object> request);

    @PostMapping("/api/bulk/medications/stock-adjustment")
    Map<String, Object> bulkStockAdjustment(@RequestBody Map<String, Object> request);
}

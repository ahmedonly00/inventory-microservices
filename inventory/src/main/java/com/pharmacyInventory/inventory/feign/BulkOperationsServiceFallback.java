package com.pharmacyInventory.inventory.feign;

import java.util.List;
import java.util.Map;

public class BulkOperationsServiceFallback implements BulkOperationsServiceClient {

    private static final String ERROR_MESSAGE = "Bulk Operations Service is currently unavailable. Please try again later.";
    
    private Map<String, Object> createErrorResponse(String operation) {
        return Map.of(
            "success", false,
            "message", ERROR_MESSAGE,
            "operation", operation
        );
    }

    @Override
    public Map<String, Object> bulkDeleteMedications(List<Long> medicationIds) {
        return createErrorResponse("bulkDeleteMedications");
    }

    @Override
    public Map<String, Object> bulkUpdatePrices(Map<String, Object> request) {
        return createErrorResponse("bulkUpdatePrices");
    }

    @Override
    public Map<String, Object> bulkStockAdjustment(Map<String, Object> request) {
        return createErrorResponse("bulkStockAdjustment");
    }
    
}

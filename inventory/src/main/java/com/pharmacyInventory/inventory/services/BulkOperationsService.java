package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.repository.MedicationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkOperationsService {

    private final MedicationsRepository medicationsRepository;

    public Map<String, Object> bulkDeleteMedications(List<Long> medicationIds) {
        log.info("Starting bulk delete for {} medications", medicationIds.size());
        
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Long medicationId : medicationIds) {
            try {
                if (medicationsRepository.existsById(medicationId)) {
                    medicationsRepository.deleteById(medicationId);
                    successCount++;
                    log.debug("Successfully deleted medication with id: {}", medicationId);
                } else {
                    failureCount++;
                    errors.add("Medication not found with id: " + medicationId);
                    log.warn("Medication not found for deletion: {}", medicationId);
                }
            } catch (Exception e) {
                failureCount++;
                errors.add("Failed to delete medication with id " + medicationId + ": " + e.getMessage());
                log.error("Error deleting medication with id: {}", medicationId, e);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("errors", errors);
        result.put("totalProcessed", medicationIds.size());
        
        log.info("Bulk delete completed. Success: {}, Failures: {}", successCount, failureCount);
        return result;
    }

    public Map<String, Object> bulkUpdatePrices(List<Map<String, Object>> priceUpdates) {
        log.info("Starting bulk price update for {} medications", priceUpdates.size());
        
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Map<String, Object> update : priceUpdates) {
            try {
                Long medicationId = Long.valueOf(update.get("medicationId").toString());
                Double newPrice = Double.valueOf(update.get("newPrice").toString());
                
                Medications medication = medicationsRepository.findById(medicationId)
                        .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));
                
                medication.setPrice(newPrice.floatValue());
                medication.setUpdatedAt(LocalDateTime.now());
                
                medicationsRepository.save(medication);
                successCount++;
                log.debug("Successfully updated price for medication with id: {}", medicationId);
                
            } catch (Exception e) {
                failureCount++;
                errors.add("Failed to update price: " + e.getMessage());
                log.error("Error updating price", e);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("errors", errors);
        result.put("totalProcessed", priceUpdates.size());
        
        log.info("Bulk price update completed. Success: {}, Failures: {}", successCount, failureCount);
        return result;
    }

    public Map<String, Object> bulkStockAdjustment(List<Map<String, Object>> stockAdjustments) {
        log.info("Starting bulk stock adjustment for {} medications", stockAdjustments.size());
        
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Map<String, Object> adjustment : stockAdjustments) {
            try {
                Long medicationId = Long.valueOf(adjustment.get("medicationId").toString());
                Integer quantityAdjustment = Integer.valueOf(adjustment.get("quantityAdjustment").toString());
                String reason = adjustment.containsKey("reason") ? adjustment.get("reason").toString() : "Bulk adjustment";
                
                Medications medication = medicationsRepository.findById(medicationId)
                        .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));
                
                Integer currentStock = medication.getStockQuantity() != null ? medication.getStockQuantity() : 0;
                Integer newStock = currentStock + quantityAdjustment;
                
                if (newStock < 0) {
                    throw new RuntimeException("Stock cannot be negative. Current: " + currentStock + ", Adjustment: " + quantityAdjustment);
                }
                
                medication.setStockQuantity(newStock);
                medication.setUpdatedAt(LocalDateTime.now());
                
                medicationsRepository.save(medication);
                successCount++;
                log.debug("Successfully adjusted stock for medication with id: {} from {} to {}", 
                         medicationId, currentStock, newStock);
                
            } catch (Exception e) {
                failureCount++;
                errors.add("Failed to adjust stock: " + e.getMessage());
                log.error("Error adjusting stock", e);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("errors", errors);
        result.put("totalProcessed", stockAdjustments.size());
        
        log.info("Bulk stock adjustment completed. Success: {}, Failures: {}", successCount, failureCount);
        return result;
    }
}

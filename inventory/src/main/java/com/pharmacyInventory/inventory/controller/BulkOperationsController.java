package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.services.BulkOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bulk")
@CrossOrigin(origins = "*")
public class BulkOperationsController {

    private final BulkOperationsService bulkOperationsService;

    @PostMapping(value = "/medications/delete")
    public ResponseEntity<?> bulkDeleteMedications(@RequestBody List<Long> medicationIds) {
        log.info("Bulk deleting {} medications", medicationIds.size());
        try {
            Map<String, Object> result = bulkOperationsService.bulkDeleteMedications(medicationIds);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error bulk deleting medications", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/medications/update-prices")
    public ResponseEntity<?> bulkUpdatePrices(@RequestBody Map<String, Object> request) {
        log.info("Bulk updating prices for medications");
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> priceUpdates = (List<Map<String, Object>>) request.get("priceUpdates");
            Map<String, Object> result = bulkOperationsService.bulkUpdatePrices(priceUpdates);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error bulk updating prices", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/stock-adjustment")
    public ResponseEntity<?> bulkStockAdjustment(@RequestBody Map<String, Object> request) {
        log.info("Bulk stock adjustment for medications");
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> stockAdjustments = (List<Map<String, Object>>) request.get("stockAdjustments");
            Map<String, Object> result = bulkOperationsService.bulkStockAdjustment(stockAdjustments);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error bulk stock adjustment", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

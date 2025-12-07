package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.Enum.StockStatus;
import com.pharmacyInventory.inventory.dtos.medications.MedicationsDTO;
import com.pharmacyInventory.inventory.services.MedicationsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medications")
@CrossOrigin(origins = "*")
public class MedicationsController {

    private final MedicationsService medicationsService;

    @GetMapping(value = "/getAllMedications/{branchId}")
    public ResponseEntity<List<MedicationsDTO>> getAllMedications(@PathVariable String branchId) {
        log.info("Fetching all medications");
        List<MedicationsDTO> medications = medicationsService.getAllMedications(branchId);
        return ResponseEntity.ok(medications);
    }

    @GetMapping(value = "/getMedicationById/{id}/{branchId}")
    public ResponseEntity<MedicationsDTO> getMedicationById(@PathVariable Long id, @PathVariable String branchId) {
        log.info("Fetching medication with id: {}", id);
        MedicationsDTO medication = medicationsService.getMedicationById(id, branchId);
        return ResponseEntity.ok(medication);
    }

    @PostMapping(value = "/createMedication/{branchId}")
    public ResponseEntity<?> createMedication(@Valid @RequestBody MedicationsDTO medicationDTO, @PathVariable String branchId) {
        try {
            MedicationsDTO created = medicationsService.createMedication(medicationDTO, branchId);
            log.info("Created new medication with id: {}", created.getMedicationId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating medication", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } 
    }

    @PutMapping(value = "/updateMedication/{id}/{branchId}")
    public ResponseEntity<?> updateMedication(@PathVariable Long id, @Valid @RequestBody MedicationsDTO medicationDTO, @PathVariable String branchId) {
        try {
            MedicationsDTO updated = medicationsService.updateMedication(id, medicationDTO, branchId);
            log.info("Updated medication with id: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating medication with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteMedication/{id}/{branchId}")
    public ResponseEntity<?> deleteMedication(@PathVariable Long id, @PathVariable String branchId) {
        try {
            medicationsService.deleteMedication(id, branchId);
            log.info("Deleted medication with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting medication with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/add-stock/{branchId}")
    public ResponseEntity<?> addStock(@PathVariable Long id, @RequestParam Integer quantity, @PathVariable String branchId) {
        try {
            medicationsService.addStock(id, quantity, branchId);
            log.info("Added {} units of stock to medication id: {}", quantity, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error adding stock to medication with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/searchMedications/{branchId}")
    public ResponseEntity<List<MedicationsDTO>> searchMedications(@RequestParam String query, @PathVariable String branchId) {
        log.info("Searching medications with query: {}", query);
        List<MedicationsDTO> medications = medicationsService.searchMedications(query, branchId);
        return ResponseEntity.ok(medications);
    }

    @GetMapping(value = "/filterMedicationsByCategory/{categoryId}/{branchId}")
    public ResponseEntity<List<MedicationsDTO>> filterMedicationsByCategory(@PathVariable Long categoryId, @PathVariable String branchId) {
        log.info("Filtering medications by category id: {}", categoryId);
        List<MedicationsDTO> medications = medicationsService.filterMedicationsByCategory(categoryId, branchId);
        return ResponseEntity.ok(medications);
    }

    @GetMapping(value = "/filterMedicationsByStatus/{stockStatus}/{branchId}")
    public ResponseEntity<List<MedicationsDTO>> filterMedicationsByStatus(@PathVariable StockStatus stockStatus, @PathVariable String branchId) {
        log.info("Filtering medications by status: {}", stockStatus);
        List<MedicationsDTO> medications = medicationsService.filterMedicationsByStatus(stockStatus, branchId);
        return ResponseEntity.ok(medications);
    }

    @PostMapping(value = "/updateStockQuantity/{medicationId}/{quantity}/{branchId}")
    public ResponseEntity<?> updateStockQuantity(@PathVariable Long medicationId, @PathVariable Integer quantity, @PathVariable String branchId) {
        try {
            MedicationsDTO updated = medicationsService.updateStockQuantity(medicationId, quantity, branchId);
            log.info("Updated stock quantity for medication id: {}", medicationId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating stock quantity for medication with id: {}", medicationId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/updateStockStatus/{medicationId}/{branchId}")
    public ResponseEntity<?> updateStockStatus(@PathVariable Long medicationId, @PathVariable String branchId) {
        try {
            medicationsService.updateStockStatus(medicationId, branchId);
            log.info("Updated stock status for medication id: {}", medicationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating stock status for medication with id: {}", medicationId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploadInventory/{branchId}")
    public ResponseEntity<?> uploadInventory(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, @PathVariable String branchId) {
        try {
            List<MedicationsDTO> medications = medicationsService.uploadInventory(file, branchId);
            log.info("Uploaded {} medications from file", medications.size());
            return ResponseEntity.ok(medications);
        } catch (Exception e) {
            log.error("Error uploading inventory file", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/downloadTemplate")
    public ResponseEntity<?> downloadTemplate() {
        try {
            byte[] template = medicationsService.downloadTemplate();
            log.info("Downloaded inventory template");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=inventory_template.xlsx")
                    .body(template);
        } catch (Exception e) {
            log.error("Error downloading template", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/exportInventory/{branchId}")
    public ResponseEntity<?> exportInventory(@PathVariable String branchId) {
        try {
            byte[] template = medicationsService.exportToExcel(branchId);
            log.info("Downloaded inventory template");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=inventory.xlsx")
                    .body(template);
        } catch (Exception e) {
            log.error("Error downloading template", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

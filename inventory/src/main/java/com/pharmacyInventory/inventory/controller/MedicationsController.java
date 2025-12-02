package com.pharmacyInventory.inventory.controller;

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

    @GetMapping(value = "/getAllMedications")
    public ResponseEntity<List<MedicationsDTO>> getAllMedications() {
        log.info("Fetching all medications");
        List<MedicationsDTO> medications = medicationsService.getAllMedications();
        return ResponseEntity.ok(medications);
    }

    @GetMapping(value = "/getMedicationById/{id}")
    public ResponseEntity<MedicationsDTO> getMedicationById(@PathVariable Long id) {
        log.info("Fetching medication with id: {}", id);
        MedicationsDTO medication = medicationsService.getMedicationById(id);
        return ResponseEntity.ok(medication);
    }

    @PostMapping(value = "/createMedication")
    public ResponseEntity<?> createMedication(@Valid @RequestBody MedicationsDTO medicationDTO) {
        try {
            MedicationsDTO created = medicationsService.createMedication(medicationDTO);
            log.info("Created new medication with id: {}", created.getMedicationId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating medication", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } 
    }

    @PutMapping(value = "/updateMedication/{id}")
    public ResponseEntity<?> updateMedication(@PathVariable Long id, @Valid @RequestBody MedicationsDTO medicationDTO) {
        try {
            MedicationsDTO updated = medicationsService.updateMedication(id, medicationDTO);
            log.info("Updated medication with id: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating medication with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteMedication/{id}")
    public ResponseEntity<?> deleteMedication(@PathVariable Long id) {
        try {
            medicationsService.deleteMedication(id);
            log.info("Deleted medication with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting medication with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/add-stock")
    public ResponseEntity<?> addStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            medicationsService.addStock(id, quantity);
            log.info("Added {} units of stock to medication id: {}", quantity, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error adding stock to medication with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/searchMedications")
    public ResponseEntity<List<MedicationsDTO>> searchMedications(@RequestParam String query) {
        log.info("Searching medications with query: {}", query);
        List<MedicationsDTO> medications = medicationsService.searchMedications(query);
        return ResponseEntity.ok(medications);
    }

    @PostMapping(value = "/uploadInventory")
    public ResponseEntity<?> uploadInventory(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            List<MedicationsDTO> medications = medicationsService.uploadInventory(file);
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
}

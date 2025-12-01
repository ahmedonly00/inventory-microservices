package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.equivalents.EquivalentsDTO;
import com.pharmacyInventory.inventory.services.EquivalentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equivalents")
@CrossOrigin(origins = "*")
public class EquivalentsController {

    private final EquivalentsService equivalentsService;

    @GetMapping(value = "/getAllEquivalents")
    public ResponseEntity<List<EquivalentsDTO>> getAllEquivalents() {
        log.info("Fetching all equivalents");
        List<EquivalentsDTO> equivalents = equivalentsService.getAllEquivalents();
        return ResponseEntity.ok(equivalents);
    }

    @GetMapping(value = "/medication/{medicationId}")
    public ResponseEntity<List<EquivalentsDTO>> getEquivalentsByMedicationId(@PathVariable Long medicationId) {
        log.info("Fetching equivalents for medication with id: {}", medicationId);
        List<EquivalentsDTO> equivalents = equivalentsService.getEquivalentsByMedicationId(medicationId);
        return ResponseEntity.ok(equivalents);
    }

    @PostMapping(value = "/createEquivalent")
    public ResponseEntity<?> createEquivalent(@Valid @RequestBody EquivalentsDTO equivalentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in create equivalent request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            EquivalentsDTO created = equivalentsService.createEquivalent(equivalentDTO);
            log.info("Created new equivalent with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating equivalent", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/updateEquivalent/{id}")
    public ResponseEntity<?> updateEquivalent(@PathVariable Long id, @Valid @RequestBody EquivalentsDTO equivalentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in update equivalent request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            EquivalentsDTO updated = equivalentsService.updateEquivalent(id, equivalentDTO);
            log.info("Updated equivalent with id: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating equivalent with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteEquivalent/{id}")
    public ResponseEntity<?> deleteEquivalent(@PathVariable Long id) {
        try {
            equivalentsService.deleteEquivalent(id);
            log.info("Deleted equivalent with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting equivalent with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

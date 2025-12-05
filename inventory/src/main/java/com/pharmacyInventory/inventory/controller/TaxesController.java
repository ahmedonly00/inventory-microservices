package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.taxes.TaxesDTO;
import com.pharmacyInventory.inventory.services.TaxesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/taxes")
@CrossOrigin(origins = "*")
public class TaxesController {

    private final TaxesService taxesService;

    @GetMapping(value="/getAllTaxes")
    public ResponseEntity<List<TaxesDTO>> getAllTaxes() {
        log.info("Fetching all taxes");
        try {
            List<TaxesDTO> taxes = taxesService.getAllTaxes();
            return ResponseEntity.ok(taxes);
        } catch (Exception e) {
            log.error("Error fetching all taxes", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value="/getTaxById/{id}")
    public ResponseEntity<TaxesDTO> getTaxById(@NonNull @PathVariable Long id) {
        log.info("Fetching tax with id: {}", id);
        try {
            TaxesDTO tax = taxesService.getTaxById(id);
            return ResponseEntity.ok(tax);
        } catch (Exception e) {
            log.error("Error fetching tax with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value="/createTax")
    public ResponseEntity<?> createTax(@Valid @RequestBody @NonNull TaxesDTO taxDTO, BindingResult bindingResult) {
        log.info("Creating new tax: {}", taxDTO.getName());
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        try {
            TaxesDTO created = taxesService.createTax(taxDTO);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating tax", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value="/updateTax/{id}")
    public ResponseEntity<?> updateTax(@NonNull @PathVariable Long id, @Valid @RequestBody @NonNull TaxesDTO taxDTO, @NonNull BindingResult bindingResult) {
        log.info("Updating tax with id: {}", id);
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        try {
            TaxesDTO updated = taxesService.updateTax(id, taxDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating tax with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value="/activate/{id}")
    public ResponseEntity<TaxesDTO> activateTax(@NonNull @PathVariable Long id) {
        log.info("Activating tax with id: {}", id);
        try {
            TaxesDTO activated = taxesService.activateTax(id);
            return ResponseEntity.ok(activated);
        } catch (Exception e) {
            log.error("Error activating tax with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value="/deactivate/{id}")
    public ResponseEntity<TaxesDTO> deactivateTax(@NonNull @PathVariable Long id) {
        log.info("Deactivating tax with id: {}", id);
        try {
            TaxesDTO deactivated = taxesService.deactivateTax(id);
            return ResponseEntity.ok(deactivated);
        } catch (Exception e) {
            log.error("Error deactivating tax with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value="/deleteTax/{id}")
    public ResponseEntity<?> deleteTax(@NonNull @PathVariable Long id) {
        log.info("Deleting tax with id: {}", id);
        try {
            taxesService.deleteTax(id);
            Map<String, String> response = Map.of("message", "Tax deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting tax with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/active")
    public ResponseEntity<List<TaxesDTO>> getActiveTaxes() {
        log.info("Fetching active taxes only");
        try {
            List<TaxesDTO> activeTaxes = taxesService.getActiveTaxes();
            return ResponseEntity.ok(activeTaxes);
        } catch (Exception e) {
            log.error("Error fetching active taxes", e);
            return ResponseEntity.badRequest().build();
        }
    }
}

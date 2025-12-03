package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.services.SuppliersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SuppliersController {

    private final SuppliersService suppliersService;

    @GetMapping("/getAllSuppliers")
    public ResponseEntity<List<SuppliersDTO>> getAllSuppliers() {
        log.info("Fetching all suppliers");
        List<SuppliersDTO> suppliers = suppliersService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping("/createSupplier")
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SuppliersDTO supplierDTO) {
        log.info("Creating new supplier");
        SuppliersDTO created = suppliersService.createSupplier(supplierDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}/updateSupplier")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @Valid @RequestBody SuppliersDTO supplierDTO) {
        log.info("Updating supplier with id: {}", id);
        SuppliersDTO updated = suppliersService.updateSupplier(id, supplierDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/deactivateSupplier")
    public ResponseEntity<?> deactivateSupplier(@PathVariable Long id) {
        log.info("Deactivating supplier with id: {}", id);
        suppliersService.deactivateSupplier(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/deleteSupplier")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        log.info("Deleting supplier with id: {}", id);
        suppliersService.deleteSupplier(id);
        return ResponseEntity.ok().build();
    }
}

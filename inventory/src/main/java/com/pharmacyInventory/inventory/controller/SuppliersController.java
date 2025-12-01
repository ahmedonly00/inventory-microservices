package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.services.SuppliersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.java.com.pharmacyInventory.inventory.feign.SupplierServiceClient;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SuppliersController {

    private final SupplierServiceClient supplierServiceClient;

    @GetMapping("/all")
    public ResponseEntity<List<SuppliersDTO>> getAllSuppliers() {
        log.info("Fetching all suppliers");
        List<SuppliersDTO> suppliers = supplierServiceClient.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SuppliersDTO supplierDTO) {
        log.info("Creating new supplier");
        SuppliersDTO created = supplierServiceClient.createSupplier(supplierDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @Valid @RequestBody SuppliersDTO supplierDTO) {
        log.info("Updating supplier with id: {}", id);
        SuppliersDTO updated = supplierServiceClient.updateSupplier(id, supplierDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateSupplier(@PathVariable Long id) {
        log.info("Deactivating supplier with id: {}", id);
        supplierServiceClient.deactivateSupplier(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        log.info("Deleting supplier with id: {}", id);
        supplierServiceClient.deleteSupplier(id);
        return ResponseEntity.ok().build();
    }
}

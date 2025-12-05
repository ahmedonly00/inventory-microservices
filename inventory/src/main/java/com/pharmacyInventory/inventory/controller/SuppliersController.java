package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.services.SuppliersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SuppliersController {

    private final SuppliersService suppliersService;

    @GetMapping("/getAllSuppliers/{branchId}")
    public ResponseEntity<List<SuppliersDTO>> getAllSuppliers(@PathVariable String branchId) {
        log.info("Fetching all suppliers for branch: {}", branchId);
        List<SuppliersDTO> suppliers = suppliersService.getAllSuppliersByBranch(branchId);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}/branch/{branchId}")
    public ResponseEntity<SuppliersDTO> getSupplierById(
            @PathVariable Long id,
            @PathVariable String branchId) {
        log.info("Fetching supplier with id: {} for branch: {}", id, branchId);
        SuppliersDTO supplier = suppliersService.getSuppliersById(id, branchId);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping("/createSupplier/byBranch/{branchId}")
    public ResponseEntity<?> createSupplier(@PathVariable String branchId, @Valid @RequestBody SuppliersDTO supplierDTO) {
        log.info("Creating new supplier for branch: {}", branchId);
        SuppliersDTO created = suppliersService.createSupplier(branchId, supplierDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}/updateSupplier/byBranch/{branchId}")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @PathVariable String branchId, @Valid @RequestBody SuppliersDTO supplierDTO) {
        log.info("Updating supplier with id: {} for branch: {}", id, branchId);
        SuppliersDTO updated = suppliersService.updateSupplier(id, supplierDTO, branchId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/deactivateSupplier/byBranch/{branchId}")
    public ResponseEntity<?> deactivateSupplier(@PathVariable Long id, @PathVariable String branchId) {
        log.info("Deactivating supplier with id: {} for branch: {}", id, branchId);
        suppliersService.deactivateSupplier(id, branchId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/deleteSupplier/byBranch/{branchId}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id, @PathVariable String branchId) {
        log.info("Deleting supplier with id: {} for branch: {}", id, branchId);
        suppliersService.deleteSupplier(id, branchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/branch/{branchId}")
    public ResponseEntity<List<SuppliersDTO>> searchSuppliers(
            @PathVariable String branchId,
            @RequestParam String query) {
        return ResponseEntity.ok(suppliersService.searchSuppliers(query, branchId));
    }
    
    @GetMapping("/active/branch/{branchId}")
    public ResponseEntity<List<SuppliersDTO>> getActiveSuppliers(@PathVariable String branchId) {
        return ResponseEntity.ok(suppliersService.getActiveSuppliers(branchId));
    }
    
    @GetMapping("/inactive/branch/{branchId}")
    public ResponseEntity<List<SuppliersDTO>> getInactiveSuppliers(@PathVariable String branchId) {
        return ResponseEntity.ok(suppliersService.getInactiveSuppliers(branchId));
    }
    
    @GetMapping("/count/branch/{branchId}")
    public ResponseEntity<Map<String, Long>> getSupplierCounts(@PathVariable String branchId) {
        Map<String, Long> counts = suppliersService.getSupplierCounts(branchId);
        return ResponseEntity.ok(counts);
    }
}

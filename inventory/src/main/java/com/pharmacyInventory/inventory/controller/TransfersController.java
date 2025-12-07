package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.transfers.TransfersDTO;
import com.pharmacyInventory.inventory.services.TransfersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class TransfersController {

    private final TransfersService transfersService;

    @GetMapping
    public ResponseEntity<List<TransfersDTO>> getAllTransfers(String branchId) {
        log.info("Fetching all transfers");
        List<TransfersDTO> transfers = transfersService.getAllTransfers(branchId);
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/{id}/{branchId}")
    public ResponseEntity<TransfersDTO> getTransferById(@PathVariable Long id, @PathVariable String branchId) {
        log.info("Fetching transfer with id: {}", id);
        TransfersDTO transfer = transfersService.getTransferById(id, branchId);
        return ResponseEntity.ok(transfer);
    }

    @PostMapping(value = "/{branchId}")
    public ResponseEntity<?> createTransfer(@Valid @RequestBody TransfersDTO transferDTO, @PathVariable String branchId) {
        try {
            TransfersDTO created = transfersService.createTransfer(transferDTO, branchId);
            log.info("Created new transfer with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating transfer", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status/{branchId}")
    public ResponseEntity<?> updateTransferStatus(@PathVariable Long id, @RequestParam String status, TransfersDTO transferDTO,@PathVariable String branchId) {
        try {
            TransfersDTO updated = transfersService.updateTransferStatus(id, status, transferDTO, branchId);
            log.info("Updated status for transfer with id: {} to {}", id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating status for transfer with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/branch/{branchId}/{toBranchId}")
    public ResponseEntity<List<TransfersDTO>> getTransfersByBranch(@PathVariable String branchId, @PathVariable String toBranchId) {
        log.info("Fetching transfers for branch with id: {}", branchId);
        List<TransfersDTO> transfers = transfersService.getTransfersByBranch(branchId, toBranchId);
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/filter/{branchId}")
    public ResponseEntity<Page<TransfersDTO>> filterByBranchId(@PathVariable String branchId, Pageable pageable) {
        log.info("Fetching transfers for branch with id: {}", branchId);
        Page<TransfersDTO> transfers = transfersService.filterByBranchId(branchId, pageable);
        return ResponseEntity.ok(transfers);
    }
}

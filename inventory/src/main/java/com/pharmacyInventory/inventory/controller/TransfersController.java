package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.transfers.TransfersDTO;
import com.pharmacyInventory.inventory.services.TransfersService;
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
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class TransfersController {

    private final TransfersService transfersService;

    @GetMapping
    public ResponseEntity<List<TransfersDTO>> getAllTransfers() {
        log.info("Fetching all transfers");
        List<TransfersDTO> transfers = transfersService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransfersDTO> getTransferById(@PathVariable Long id) {
        log.info("Fetching transfer with id: {}", id);
        TransfersDTO transfer = transfersService.getTransferById(id);
        return ResponseEntity.ok(transfer);
    }

    @PostMapping
    public ResponseEntity<?> createTransfer(@Valid @RequestBody TransfersDTO transferDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in create transfer request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            TransfersDTO created = transfersService.createTransfer(transferDTO);
            log.info("Created new transfer with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating transfer", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTransferStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            TransfersDTO updated = transfersService.updateTransferStatus(id, status);
            log.info("Updated status for transfer with id: {} to {}", id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating status for transfer with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<TransfersDTO>> getTransfersByBranch(@PathVariable Long branchId) {
        log.info("Fetching transfers for branch with id: {}", branchId);
        List<TransfersDTO> transfers = transfersService.getTransfersByBranch(branchId);
        return ResponseEntity.ok(transfers);
    }
}

package com.pharmacyInventory.salesService.controller;

import com.pharmacyInventory.salesService.dtos.transactions.TransactionsDTO;
import com.pharmacyInventory.salesService.services.TransactionsService;
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
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @GetMapping
    public ResponseEntity<List<TransactionsDTO>> getAllTransactions() {
        log.info("Fetching all transactions");
        List<TransactionsDTO> transactions = transactionsService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionsDTO> getTransactionById(@PathVariable Long id) {
        log.info("Fetching transaction with id: {}", id);
        TransactionsDTO transaction = transactionsService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionsDTO transactionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in create transaction request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            TransactionsDTO created = transactionsService.createTransaction(transactionDTO);
            log.info("Created new transaction with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating transaction", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/medication/{medicationId}")
    public ResponseEntity<List<TransactionsDTO>> getTransactionsByMedication(@PathVariable Long medicationId) {
        log.info("Fetching transactions for medication with id: {}", medicationId);
        List<TransactionsDTO> transactions = transactionsService.getTransactionsByMedication(medicationId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionsDTO>> getTransactionsByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        log.info("Fetching transactions between {} and {}", startDate, endDate);
        List<TransactionsDTO> transactions = transactionsService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}

package com.pharmacyInventory.salesService.controller;

import com.pharmacyInventory.salesService.dtos.sales.SalesDTO;
import com.pharmacyInventory.salesService.services.SalesService;
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
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public ResponseEntity<List<SalesDTO>> getAllSales() {
        log.info("Fetching all sales");
        List<SalesDTO> sales = salesService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody SalesDTO salesDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in create sale request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            SalesDTO created = salesService.createSale(salesDTO);
            log.info("Created new sale with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating sale", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesDTO> getSaleById(@PathVariable Long id) {
        log.info("Fetching sale with id: {}", id);
        SalesDTO sale = salesService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }
}

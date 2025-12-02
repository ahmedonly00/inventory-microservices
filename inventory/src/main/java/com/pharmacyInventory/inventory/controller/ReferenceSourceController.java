package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.ReferenceSource.ReferenceSourceDTO;
import com.pharmacyInventory.inventory.services.ReferenceSourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reference-sources")
@CrossOrigin(origins = "*")
public class ReferenceSourceController {
    
    private final ReferenceSourceService referenceSourceService;

    
    @GetMapping("/all")
    public ResponseEntity<List<ReferenceSourceDTO>> getAllReferenceSources() {
        log.info("Fetching all reference sources");
        List<ReferenceSourceDTO> referenceSources = referenceSourceService.getAllReferenceSources();
        return ResponseEntity.ok(referenceSources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReferenceSourceDTO> getReferenceSourceById(@PathVariable Long id) {
        log.info("Fetching reference source with id: {}", id);
        return referenceSourceService.getReferenceSourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReferenceSourceDTO> createReferenceSource(
            @Valid @RequestBody ReferenceSourceDTO referenceSourceDTO) {
        log.info("Creating new reference source");
        ReferenceSourceDTO created = referenceSourceService.createReferenceSource(referenceSourceDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReferenceSourceDTO> updateReferenceSource(
            @PathVariable Long id, 
            @Valid @RequestBody ReferenceSourceDTO referenceSourceDTO) {
        log.info("Updating reference source with id: {}", id);
        ReferenceSourceDTO updated = referenceSourceService.updateReferenceSource(id, referenceSourceDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferenceSource(@PathVariable Long id) {
        log.info("Deleting reference source with id: {}", id);
        referenceSourceService.deleteReferenceSource(id);
        return ResponseEntity.noContent().build();
    }
    
}

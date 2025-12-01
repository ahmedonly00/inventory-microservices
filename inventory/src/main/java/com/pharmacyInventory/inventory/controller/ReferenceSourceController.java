package com.pharmacyInventory.inventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacyInventory.inventory.dtos.ReferenceSource.ReferenceSourceDTO;
import com.pharmacyInventory.inventory.services.ReferenceSourceService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reference-sources")
@RequiredArgsConstructor
public class ReferenceSourceController {
    
    private final ReferenceSourceService referenceSourceService;
    
    
    @GetMapping(value= "/getAllReferenceSources")
    public List<ReferenceSourceDTO> getAllReferenceSources() {
        return referenceSourceService.getAllReferenceSources();
    }

    @GetMapping(value= "/getReferenceSourceById/{id}")
    public Optional<ReferenceSourceDTO> getReferenceSourceById(@PathVariable Long id) {
        return referenceSourceService.getReferenceSourceById(id);
    }

    @PostMapping(value= "/createReferenceSource")
    public ReferenceSourceDTO createReferenceSource(@RequestBody ReferenceSourceDTO referenceSourceDTO) {
        return referenceSourceService.createReferenceSource(referenceSourceDTO);
    }

    @PutMapping(value= "/updateReferenceSource/{id}")
    public ReferenceSourceDTO updateReferenceSource(@PathVariable Long id, @RequestBody ReferenceSourceDTO referenceSourceDTO) {
        return referenceSourceService.updateReferenceSource(id, referenceSourceDTO);
    }

    @DeleteMapping(value= "/deleteReferenceSource/{id}")
    public void deleteReferenceSource(@PathVariable Long id) {
        referenceSourceService.deleteReferenceSource(id);
    }
    
}

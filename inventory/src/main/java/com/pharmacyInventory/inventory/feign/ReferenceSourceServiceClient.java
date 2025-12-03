package com.pharmacyInventory.inventory.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.pharmacyInventory.inventory.config.FeignConfig;
import com.pharmacyInventory.inventory.dtos.ReferenceSource.ReferenceSourceDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;


@FeignClient(name = "reference-source-service", 
            configuration = FeignConfig.class,
            fallback = ReferenceSourceServiceFallback.class)
public interface ReferenceSourceServiceClient {

    @GetMapping("/api/reference-sources")
    public List<ReferenceSourceDTO> getAllReferenceSources();

    @GetMapping("/api/reference-sources/{id}")
    public ReferenceSourceDTO getReferenceSourceById(@PathVariable Long id);

    @PostMapping("/api/reference-sources")
    public ReferenceSourceDTO createReferenceSource(@RequestBody ReferenceSourceDTO referenceSourceDTO);

    @PutMapping("/api/reference-sources/{id}")
    public ReferenceSourceDTO updateReferenceSource(
            @PathVariable Long id, 
            @RequestBody ReferenceSourceDTO referenceSourceDTO);

    @DeleteMapping("/api/reference-sources/{id}")
    public void deleteReferenceSource(@PathVariable Long id);
    
}

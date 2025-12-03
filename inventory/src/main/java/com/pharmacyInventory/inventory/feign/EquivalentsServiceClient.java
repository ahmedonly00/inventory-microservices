package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.pharmacyInventory.inventory.config.FeignConfig;
import com.pharmacyInventory.inventory.dtos.equivalents.EquivalentsDTO;

@FeignClient(name = "equivalents-service", configuration = FeignConfig.class)
public interface EquivalentsServiceClient {
    
    @GetMapping("/api/equivalents")
    public List<EquivalentsDTO> getAllEquivalents();

    @GetMapping("/api/equivalents/{id}")
    public EquivalentsDTO getEquivalentById(@PathVariable Long id);

    @PostMapping("/api/equivalents")
    public EquivalentsDTO createEquivalent(@RequestBody EquivalentsDTO equivalentsDTO);

    @PutMapping("/api/equivalents/{id}")
    public EquivalentsDTO updateEquivalent(@PathVariable Long id, @RequestBody EquivalentsDTO equivalentsDTO);

    @DeleteMapping("/api/equivalents/{id}")
    public void deleteEquivalent(@PathVariable Long id);
}

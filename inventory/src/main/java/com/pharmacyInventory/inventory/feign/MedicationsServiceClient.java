package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pharmacyInventory.inventory.dtos.medications.MedicationsDTO;

import java.util.List;

@FeignClient(name = "medications-service", url = "${medications.service.url}")
public interface MedicationsServiceClient {

    @GetMapping("/api/medications")
    public List<MedicationsDTO> getAllMedications();

    @GetMapping("/api/medications/{id}")
    public MedicationsDTO getMedicationById(@PathVariable Long id);

    @PostMapping("/api/medications")
    public MedicationsDTO createMedication(@RequestBody MedicationsDTO medicationsDTO);

    @PutMapping("/api/medications/{id}")
    public MedicationsDTO updateMedication(@PathVariable Long id, @RequestBody MedicationsDTO medicationsDTO);

    @DeleteMapping("/api/medications/{id}")
    public void deleteMedication(@PathVariable Long id);

    @GetMapping("/api/medications/template")
    public byte[] downloadTemplate();

    @PostMapping("/api/medications/upload")
    public List<MedicationsDTO> uploadInventory(@RequestParam("file") MultipartFile file);

    @GetMapping("/api/medications/search")
    public List<MedicationsDTO> searchMedications(@RequestParam String query);

    @PostMapping("/api/medications/{id}/add-stock")
    public void addStock(@PathVariable Long id, @RequestParam Integer quantity);

    @PostMapping("/api/medications/{id}/remove-stock")
    public void removeStock(@PathVariable Long id, @RequestParam Integer quantity);


    
}

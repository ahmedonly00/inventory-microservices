package com.pharmacyInventory.inventory.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "supplier-service", 
            url = "${supplier.service.url}",
            fallback = SupplierServiceFallback.class)
public interface SupplierServiceClient {
    
    @GetMapping("/api/suppliers")
    public List<SuppliersDTO> getAllSuppliers();

    @GetMapping("/api/suppliers/{id}")
    public SuppliersDTO getSupplierById(@PathVariable Long id);

    @PostMapping("/api/suppliers")
    public SuppliersDTO createSupplier(@RequestBody SuppliersDTO supplierDTO);

    @PutMapping("/api/suppliers/{id}")
    public SuppliersDTO updateSupplier(@PathVariable Long id, @RequestBody SuppliersDTO supplierDTO);

    @DeleteMapping("/api/suppliers/{id}")
    public void deleteSupplier(@PathVariable Long id);

    @PatchMapping("/api/suppliers/{id}/deactivate")
    public void deactivateSupplier(@PathVariable Long id);
}

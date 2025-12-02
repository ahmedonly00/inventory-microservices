package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.pharmacyInventory.inventory.dtos.Brands.BrandsDTO;

import java.util.List;

@FeignClient(name = "brand-service", url = "${brand.service.url}")
public interface BrandServiceClient {
    
    @GetMapping("/api/brands")
    public List<BrandsDTO> getAllBrands();

    @GetMapping("/api/brands/{id}")
    public BrandsDTO getBrandById(@PathVariable Long id);

    @PostMapping("/api/brands")
    public BrandsDTO createBrand(@RequestBody BrandsDTO brandsDTO);

    @PutMapping("/api/brands/{id}")
    public BrandsDTO updateBrand(@PathVariable Long id, @RequestBody BrandsDTO brandsDTO);

    @DeleteMapping("/api/brands/{id}")
    public void deleteBrand(@PathVariable Long id);
}

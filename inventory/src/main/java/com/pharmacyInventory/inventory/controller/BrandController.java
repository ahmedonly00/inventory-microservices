package com.pharmacyInventory.inventory.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.pharmacyInventory.inventory.dtos.Brands.BrandsDTO;
import com.pharmacyInventory.inventory.services.BrandService;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;


    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping(value="/getAllBrands")
    public List<BrandsDTO> getAllBrands() {
        return brandService.getAllBrands();
    }

    @GetMapping(value="/getBrandById/{id}")
    public Optional<BrandsDTO> getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @PostMapping(value="/createBrand")
    public BrandsDTO createBrand(@RequestBody BrandsDTO brandDTO) {
        return brandService.createBrand(brandDTO);
    }

    @PutMapping(value="/updateBrand/{id}")
    public BrandsDTO updateBrand(@PathVariable Long id, @RequestBody BrandsDTO brandDTO) {
        return brandService.updateBrand(id, brandDTO);
    }

    @DeleteMapping(value="/deleteBrand/{id}")
    public void deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
    }

    
}

package com.pharmacyInventory.inventory.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.pharmacyInventory.inventory.dtos.categories.CategoriesDTO;

@FeignClient(name = "categories-service", url = "${categories.service.url}")
public interface CategoriesServiceClient {
    
    @GetMapping("/api/categories")
    public List<CategoriesDTO> getAllCategories();

    @GetMapping("/api/categories/{id}")
    public CategoriesDTO getCategoryById(@PathVariable Long id);

    @PostMapping("/api/categories")
    public CategoriesDTO createCategory(@RequestBody CategoriesDTO categoriesDTO);

    @PutMapping("/api/categories/{id}")
    public CategoriesDTO updateCategory(@PathVariable Long id, @RequestBody CategoriesDTO categoriesDTO);

    @DeleteMapping("/api/categories/{id}")
    public void deleteCategory(@PathVariable Long id);
    
}

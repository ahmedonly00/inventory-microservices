package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.Enum.CategoryType;
import com.pharmacyInventory.inventory.dtos.categories.CategoriesDTO;
import com.pharmacyInventory.inventory.services.CategoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping(value = "/getAllCategories/{branchId}")
    public ResponseEntity<List<CategoriesDTO>> getAllCategories(@PathVariable String branchId) {
        log.info("Fetching all categories");
        List<CategoriesDTO> categories = categoriesService.getAllCategories(branchId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/getAllForms/{branchId}")
    public ResponseEntity<List<CategoriesDTO>> getAllForms(@PathVariable String branchId) {
        log.info("Fetching all forms");
        List<CategoriesDTO> forms = categoriesService.getAllForms(branchId);
        return ResponseEntity.ok(forms);
    }

    @PostMapping(value = "/createCategory/{branchId}")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoriesDTO categoryDTO, @PathVariable String branchId) {
        try {
            CategoriesDTO created = categoriesService.createCategory(categoryDTO, branchId);
            log.info("Created new category with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating category", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/updateCategory/{id}/{branchId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoriesDTO categoryDTO, @PathVariable String branchId) {
        try {
            CategoriesDTO updated = categoriesService.updateCategory(id, categoryDTO, branchId);
            log.info("Updated category with id: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating category with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/deactivateCategory/{id}/{branchId}")
    public ResponseEntity<?> deactivateCategory(@PathVariable Long id, @PathVariable String branchId) {
        try {
            categoriesService.deactivateCategory(id, branchId);
            log.info("Deactivated category with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deactivating category with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteCategory/{id}/{branchId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, @PathVariable String branchId) {
        try {
            categoriesService.deleteCategory(id, branchId);
            log.info("Deleted category with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting category with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/getCategoriesByType/{type}/{branchId}")
    public ResponseEntity<List<CategoriesDTO>> getCategoriesByType(@PathVariable CategoryType type, @PathVariable String branchId) {
        log.info("Fetching categories of type: {}", type);
        List<CategoriesDTO> categories = categoriesService.getCategoriesByType(type, branchId);
        return ResponseEntity.ok(categories);
    }
}

package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.categories.CategoriesDTO;
import com.pharmacyInventory.inventory.services.CategoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping(value = "/getAllCategories")
    public ResponseEntity<List<CategoriesDTO>> getAllCategories() {
        log.info("Fetching all categories");
        List<CategoriesDTO> categories = categoriesService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping(value = "/createCategory")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoriesDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in create category request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            CategoriesDTO created = categoriesService.createCategory(categoryDTO);
            log.info("Created new category with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating category", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/updateCategory/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoriesDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Invalid request";
            var fieldError = bindingResult.getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                errorMessage = fieldError.getDefaultMessage();
            }
            log.warn("Validation errors in update category request: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            CategoriesDTO updated = categoriesService.updateCategory(id, categoryDTO);
            log.info("Updated category with id: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating category with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/deactivateCategory/{id}")
    public ResponseEntity<?> deactivateCategory(@PathVariable Long id) {
        try {
            categoriesService.deactivateCategory(id);
            log.info("Deactivated category with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deactivating category with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoriesService.deleteCategory(id);
            log.info("Deleted category with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting category with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

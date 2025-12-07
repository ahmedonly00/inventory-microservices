package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.Enum.CategoryType;
import com.pharmacyInventory.inventory.dtos.categories.CategoriesDTO;
import com.pharmacyInventory.inventory.mapper.CategoriesMapper;
import com.pharmacyInventory.inventory.model.Categories;
import com.pharmacyInventory.inventory.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    public List<CategoriesDTO> getAllCategories(String branchId) {
        log.info("Fetching all categories");
        List<Categories> categories = categoriesRepository.findByBranchId(branchId);
        return categoriesMapper.toCategoriesDTO(categories);
    }

    public List<CategoriesDTO> getAllForms(String branchId) {
        log.info("Fetching all forms");
        List<Categories> forms = categoriesRepository.findByType(CategoryType.FORM, branchId);
        return categoriesMapper.toCategoriesDTO(forms);
    }

    public CategoriesDTO createCategory(CategoriesDTO categoryDTO, String branchId) {
        log.info("Creating new category: {}", categoryDTO.getName());
        
        // Check if category with same name already exists
        if (categoriesRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category with name '" + categoryDTO.getName() + "' already exists");
        }

        Categories category = categoriesMapper.toCategories(categoryDTO);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category.setIsActive(true);

        category.setBranchId(branchId);
        Categories saved = categoriesRepository.save(category);
        log.info("Created category with id: {}", saved.getId());
        return categoriesMapper.toCategoriesDTO(saved);
    }

    public CategoriesDTO updateCategory(Long id, CategoriesDTO categoryDTO, String branchId) {
        log.info("Updating category with id: {}", id);
        
        Categories existing = categoriesRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Update fields
        existing.setName(categoryDTO.getName());
        existing.setType(categoryDTO.getType());
        existing.setColor(categoryDTO.getColor());
        existing.setDescription(categoryDTO.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());

        existing.setBranchId(branchId);
        Categories updated = categoriesRepository.save(existing);
        log.info("Updated category with id: {}", id);
        return categoriesMapper.toCategoriesDTO(updated);
    }

    public void deactivateCategory(Long id, String branchId) {
        log.info("Deactivating category with id: {}", id);
        
        Categories category = categoriesRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setIsActive(false);
        category.setUpdatedAt(LocalDateTime.now());

        categoriesRepository.save(category);
        log.info("Deactivated category with id: {}", id);
    }

    public void deleteCategory(Long id, String branchId) {
        log.info("Deleting category with id: {}", id);
        
        Categories category = categoriesRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoriesRepository.delete(category);
        log.info("Deleted category with id: {}", id);
    }
    
    public List<CategoriesDTO> getCategoriesByType(CategoryType type, String branchId) {
        log.info("Fetching categories of type: {}", type);
        List<Categories> categories = categoriesRepository.findByType(type, branchId);
        return categoriesMapper.toCategoriesDTO(categories);
    }
}

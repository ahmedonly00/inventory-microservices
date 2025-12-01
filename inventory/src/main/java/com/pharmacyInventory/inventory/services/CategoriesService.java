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

    public List<CategoriesDTO> getAllCategories() {
        log.info("Fetching all categories");
        List<Categories> categories = categoriesRepository.findAll();
        return categoriesMapper.toCategoriesDTO(categories);
    }

    public CategoriesDTO createCategory(CategoriesDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getName());
        
        // Check if category with same name already exists
        if (categoriesRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category with name '" + categoryDTO.getName() + "' already exists");
        }

        Categories category = categoriesMapper.toCategories(categoryDTO);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category.setIsActive(true);

        Categories saved = categoriesRepository.save(category);
        log.info("Created category with id: {}", saved.getId());
        return categoriesMapper.toCategoriesDTO(saved);
    }

    public CategoriesDTO updateCategory(Long id, CategoriesDTO categoryDTO) {
        log.info("Updating category with id: {}", id);
        
        Categories existing = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Update fields
        existing.setName(categoryDTO.getName());
        existing.setType(categoryDTO.getType());
        existing.setColor(categoryDTO.getColor());
        existing.setDescription(categoryDTO.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());

        Categories updated = categoriesRepository.save(existing);
        log.info("Updated category with id: {}", id);
        return categoriesMapper.toCategoriesDTO(updated);
    }

    public void deactivateCategory(Long id) {
        log.info("Deactivating category with id: {}", id);
        
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setIsActive(false);
        category.setUpdatedAt(LocalDateTime.now());

        categoriesRepository.save(category);
        log.info("Deactivated category with id: {}", id);
    }

    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoriesRepository.delete(category);
        log.info("Deleted category with id: {}", id);
    }
    
    public List<CategoriesDTO> getCategoriesByType(CategoryType type) {
        log.info("Fetching categories of type: {}", type);
        List<Categories> categories = categoriesRepository.findByType(type);
        return categoriesMapper.toCategoriesDTO(categories);
    }
}

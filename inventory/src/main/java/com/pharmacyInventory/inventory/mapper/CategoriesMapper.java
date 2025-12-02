package com.pharmacyInventory.inventory.mapper;

import com.pharmacyInventory.inventory.dtos.categories.CategoriesDTO;
import com.pharmacyInventory.inventory.model.Categories;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriesMapper {

    public List<CategoriesDTO> toCategoriesDTO(List<Categories> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(this::toCategoriesDTO)
                .collect(Collectors.toList());
    }

    public CategoriesDTO toCategoriesDTO(Categories category) {
        if(category == null){
            return null;
        }

        return CategoriesDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .color(category.getColor())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .medicationIds(category.getMedications() != null ? 
                    category.getMedications().stream().map(m -> m.getMedicationId()).collect(Collectors.toList()) : null)
                .build();
    }

    public Categories toCategories(CategoriesDTO categoriesDTO) {
        if(categoriesDTO == null) {
            return null;
        }

        Categories category = new Categories();
        category.setId(categoriesDTO.getId());
        category.setName(categoriesDTO.getName());
        category.setType(categoriesDTO.getType());
        category.setColor(categoriesDTO.getColor());
        category.setDescription(categoriesDTO.getDescription());
        category.setIsActive(categoriesDTO.getIsActive());
                
        return category;
    } 
    
}

package com.pharmacyInventory.inventory.mapper;

import com.pharmacyInventory.inventory.dtos.medications.MedicationsDTO;
import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.model.Categories;
import com.pharmacyInventory.inventory.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicationsMapper {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public List<MedicationsDTO> toMedicationsDTO(List<Medications> medications) {
        if (medications == null) {
            return null;
        }
        return medications.stream()
                .map(this::toMedicationsDTO)
                .collect(Collectors.toList());
    }

    public MedicationsDTO toMedicationsDTO(Medications medication) {
        if(medication == null){
            return null;
        }

        return MedicationsDTO.builder()
                .medicationId(medication.getMedicationId())
                .name(medication.getName())
                .form(medication.getForm() != null ? medication.getForm().getName() : null)
                .formId(medication.getForm() != null ? medication.getForm().getId() : null)
                .formName(medication.getForm() != null ? medication.getForm().getName() : null)
                .strength(medication.getStrength())
                .stockQuantity(medication.getStockQuantity())
                .reorderLevel(medication.getReorderLevel())
                .price(medication.getPrice())
                .batchNumber(medication.getBatchNumber())
                .expiryDate(medication.getExpiryDate())
                .isActive(medication.getIsActive())
                .createdAt(medication.getCreatedAt())
                .updatedAt(medication.getUpdatedAt())
                .branchId(medication.getBranchId())
                .categoryId(medication.getCategory() != null ? medication.getCategory().getId() : null)
                .categoryName(medication.getCategory() != null ? medication.getCategory().getName() : null)
                .supplierId(medication.getSuppliers() != null ? medication.getSuppliers().getId() : null)
                .supplierName(medication.getSuppliers() != null ? medication.getSuppliers().getName() : null)
                .build();
    }

    public Medications toMedications(MedicationsDTO medicationsDTO) {
        if(medicationsDTO == null) {
            return null;
        }

        Medications medication = new Medications();
        medication.setMedicationId(medicationsDTO.getMedicationId());
        medication.setName(medicationsDTO.getName());
        /// Set category
        if (medicationsDTO.getCategoryId() != null) {
            Long categoryId = medicationsDTO.getCategoryId();
            if (categoryId != null) {
                Categories category = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
                medication.setCategory(category);
            }
        }
        
        // Set form
        if (medicationsDTO.getFormId() != null) {
            Long formId = medicationsDTO.getFormId();
            if (formId != null) {
                Categories form = categoriesRepository.findById(formId)
                    .orElseThrow(() -> new RuntimeException("Form not found with id: " + formId));
                medication.setForm(form);
            }
        }
        medication.setStrength(medicationsDTO.getStrength());
        medication.setStockQuantity(medicationsDTO.getStockQuantity());
        medication.setReorderLevel(medicationsDTO.getReorderLevel());
        medication.setPrice(medicationsDTO.getPrice());
        medication.setBatchNumber(medicationsDTO.getBatchNumber());
        medication.setExpiryDate(medicationsDTO.getExpiryDate());
        medication.setIsActive(medicationsDTO.getIsActive());
        medication.setCreatedAt(medicationsDTO.getCreatedAt());
        medication.setUpdatedAt(medicationsDTO.getUpdatedAt());
                
        return medication;
    } 
    
}

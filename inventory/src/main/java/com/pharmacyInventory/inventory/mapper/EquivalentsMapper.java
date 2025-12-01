package com.inventory.pharmacyInventory.mapper;

import com.inventory.pharmacyInventory.dtos.equivalents.EquivalentsDTO;
import com.inventory.pharmacyInventory.model.Equivalents;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EquivalentsMapper {

    public List<EquivalentsDTO> toEquivalentsDTO(List<Equivalents> equivalents) {
        if (equivalents == null) {
            return null;
        }
        return equivalents.stream()
                .map(this::toEquivalentsDTO)
                .collect(Collectors.toList());
    }

    public EquivalentsDTO toEquivalentsDTO(Equivalents equivalent) {
        if(equivalent == null){
            return null;
        }

        return EquivalentsDTO.builder()
                .id(equivalent.getId())
                .inn(equivalent.getInn())
                .form(equivalent.getForm())
                .strength(equivalent.getStrength())
                .brands(equivalent.getBrands())
                .source(equivalent.getSource())
                .createdAt(equivalent.getCreatedAt())
                .originalMedicationId(equivalent.getOriginalMedication() != null ? equivalent.getOriginalMedication().getMedicationId() : null)
                .originalMedicationName(equivalent.getOriginalMedication() != null ? equivalent.getOriginalMedication().getName() : null)
                .equivalentMedicationId(equivalent.getEquivalentMedication() != null ? equivalent.getEquivalentMedication().getMedicationId() : null)
                .equivalentMedicationName(equivalent.getEquivalentMedication() != null ? equivalent.getEquivalentMedication().getName() : null)
                .build();
    }

    public Equivalents toEquivalents(EquivalentsDTO equivalentsDTO) {
        if(equivalentsDTO == null) {
            return null;
        }

        Equivalents equivalent = new Equivalents();
        equivalent.setId(equivalentsDTO.getId());
        equivalent.setInn(equivalentsDTO.getInn());
        equivalent.setForm(equivalentsDTO.getForm());
        equivalent.setStrength(equivalentsDTO.getStrength());
        equivalent.setBrands(equivalentsDTO.getBrands());
        equivalent.setSource(equivalentsDTO.getSource());
        equivalent.setCreatedAt(equivalentsDTO.getCreatedAt());
                
        return equivalent;
    } 
    
}

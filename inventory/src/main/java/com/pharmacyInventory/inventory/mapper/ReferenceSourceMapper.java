package com.inventory.pharmacyInventory.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.inventory.pharmacyInventory.dtos.ReferenceSource.ReferenceSourceDTO;
import com.inventory.pharmacyInventory.model.ReferenceSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReferenceSourceMapper {
    
    public ReferenceSourceDTO toReferenceSourceDTO(ReferenceSource referenceSource) {
        if (referenceSource == null) {
            return null;
        }
        return ReferenceSourceDTO.builder()
                .id(referenceSource.getId())
                .name(referenceSource.getName())
                .build();
    }
    
    public List<ReferenceSourceDTO> toReferenceSourceDTO(List<ReferenceSource> referenceSources) {
        if (referenceSources == null) {
            return null;
        }
        return referenceSources.stream()
                .map(this::toReferenceSourceDTO)
                .collect(Collectors.toList());
    }
    
    public ReferenceSource toReferenceSource(ReferenceSourceDTO referenceSourceDTO) {
        if (referenceSourceDTO == null) {
            return null;
        }
        return ReferenceSource.builder()
                .id(referenceSourceDTO.getId())
                .name(referenceSourceDTO.getName())
                .build();
    }
}

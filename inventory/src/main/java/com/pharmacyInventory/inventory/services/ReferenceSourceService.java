package com.pharmacyInventory.inventory.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pharmacyInventory.inventory.dtos.ReferenceSource.ReferenceSourceDTO;
import com.pharmacyInventory.inventory.mapper.ReferenceSourceMapper;
import com.pharmacyInventory.inventory.model.ReferenceSource;
import com.pharmacyInventory.inventory.repository.ReferenceSourceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReferenceSourceService {

    private final ReferenceSourceRepository referenceSourceRepository;
    private final ReferenceSourceMapper referenceSourceMapper;

    public List<ReferenceSourceDTO> getAllReferenceSources() {
        log.info("Fetching all reference sources");
        return referenceSourceMapper.toReferenceSourceDTO(referenceSourceRepository.findAll());
    }

    public Optional<ReferenceSourceDTO> getReferenceSourceById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Reference source ID cannot be null");
        }
        log.info("Fetching reference source with id: {}", id);
        return referenceSourceRepository.findById(id)
                .map(referenceSourceMapper::toReferenceSourceDTO);
    }

    public ReferenceSourceDTO createReferenceSource(ReferenceSourceDTO referenceSourceDTO) {
        log.info("Creating reference source: {}", referenceSourceDTO);
        ReferenceSource referenceSource = referenceSourceMapper.toReferenceSource(referenceSourceDTO);
        return referenceSourceMapper.toReferenceSourceDTO(referenceSourceRepository.save(referenceSource));
    }

    public ReferenceSourceDTO updateReferenceSource(Long id, ReferenceSourceDTO referenceSourceDTO) {
        log.info("Updating reference source with id: {}", id);
        ReferenceSource existingReferenceSource = referenceSourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reference source not found"));
        existingReferenceSource.setName(referenceSourceDTO.getName());
        return referenceSourceMapper.toReferenceSourceDTO(referenceSourceRepository.save(existingReferenceSource));
    }

    public void deleteReferenceSource(Long id) {
        log.info("Deleting reference source with id: {}", id);
        referenceSourceRepository.deleteById(id);
    }
    
}

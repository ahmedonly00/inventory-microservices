package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.equivalents.EquivalentsDTO;
import com.pharmacyInventory.inventory.exception.ResourceNotFoundException;
import com.pharmacyInventory.inventory.mapper.EquivalentsMapper;
import com.pharmacyInventory.inventory.model.Brands;
import com.pharmacyInventory.inventory.model.Equivalents;
import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.model.ReferenceSource;
import com.pharmacyInventory.inventory.repository.BrandRepository;
import com.pharmacyInventory.inventory.repository.EquivalentsRepository;
import com.pharmacyInventory.inventory.repository.MedicationsRepository;
import com.pharmacyInventory.inventory.repository.ReferenceSourceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquivalentsService {

    private final EquivalentsRepository equivalentsRepository;
    private final MedicationsRepository medicationsRepository;
    private final EquivalentsMapper equivalentsMapper;
    private final ReferenceSourceRepository referenceSourceRepository;
    private final BrandRepository brandRepository;
    


    public List<EquivalentsDTO> getAllEquivalents() {
        log.info("Fetching all equivalents");
        List<Equivalents> equivalents = equivalentsRepository.findAll();
        return equivalentsMapper.toEquivalentsDTO(equivalents);
    }

    public List<EquivalentsDTO> getEquivalentsByMedicationId(Long medicationId) {
        log.info("Fetching equivalents for medication with id: {}", medicationId);
        
        Medications medication = medicationsRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));
        
        List<Equivalents> equivalents = equivalentsRepository.findByOriginalMedication(medication);
        return equivalentsMapper.toEquivalentsDTO(equivalents);
    }

    public EquivalentsDTO createEquivalent(EquivalentsDTO equivalentDTO) {
        log.info("Creating new equivalent: {} - {}", equivalentDTO.getInn(), equivalentDTO.getStrength());
        
        // Validate medications exist
        Medications originalMedication = medicationsRepository.findById(equivalentDTO.getOriginalMedicationId())
                .orElseThrow(() -> new RuntimeException("Original medication not found with id: " + equivalentDTO.getOriginalMedicationId()));
        
        Medications equivalentMedication = medicationsRepository.findById(equivalentDTO.getEquivalentMedicationId())
                .orElseThrow(() -> new RuntimeException("Equivalent medication not found with id: " + equivalentDTO.getEquivalentMedicationId()));

        ReferenceSource referenceSource = referenceSourceRepository.findById(equivalentDTO.getReferenceSourceId())
                .orElseThrow(() -> new RuntimeException("Reference source not found with id: " + equivalentDTO.getReferenceSourceId()));

        // Validate all brands exist
        List<Brands> brands = brandRepository.findAllById(equivalentDTO.getBrandIds());
        if (brands.size() != equivalentDTO.getBrandIds().size()) {
            throw new ResourceNotFoundException("One or more brands not found");
        }
                
        Equivalents equivalent = equivalentsMapper.toEquivalents(equivalentDTO);
        equivalent.setOriginalMedication(originalMedication);
        equivalent.setEquivalentMedication(equivalentMedication);
        equivalent.setCreatedAt(LocalDateTime.now());

        Equivalents saved = equivalentsRepository.save(equivalent);
        log.info("Created equivalent with id: {}", saved.getId());
        return equivalentsMapper.toEquivalentsDTO(saved);
    }

    public EquivalentsDTO updateEquivalent(Long id, EquivalentsDTO equivalentDTO) {
        log.info("Updating equivalent with id: {}", id);
        
        Equivalents existing = equivalentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equivalent not found with id: " + id));

        // Update fields
        existing.setInn(equivalentDTO.getInn());
        existing.setForm(equivalentDTO.getForm());
        existing.setStrength(equivalentDTO.getStrength());
        existing.setBrandNames(equivalentDTO.getBrandNames());
        existing.setReferenceSourceName(equivalentDTO.getReferenceSourceName());

        // Update medications if changed
        if (!equivalentDTO.getOriginalMedicationId().equals(existing.getOriginalMedication().getMedicationId())) {
            Medications originalMedication = medicationsRepository.findById(equivalentDTO.getOriginalMedicationId())
                    .orElseThrow(() -> new RuntimeException("Original medication not found with id: " + equivalentDTO.getOriginalMedicationId()));
            existing.setOriginalMedication(originalMedication);
        }

        if (!equivalentDTO.getEquivalentMedicationId().equals(existing.getEquivalentMedication().getMedicationId())) {
            Medications equivalentMedication = medicationsRepository.findById(equivalentDTO.getEquivalentMedicationId())
                    .orElseThrow(() -> new RuntimeException("Equivalent medication not found with id: " + equivalentDTO.getEquivalentMedicationId()));
            existing.setEquivalentMedication(equivalentMedication);
        }

        Equivalents updated = equivalentsRepository.save(existing);
        log.info("Updated equivalent with id: {}", id);
        return equivalentsMapper.toEquivalentsDTO(updated);
    }

    public void deleteEquivalent(Long id) {
        log.info("Deleting equivalent with id: {}", id);
        
        Equivalents equivalent = equivalentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equivalent not found with id: " + id));

        equivalentsRepository.delete(equivalent);
        log.info("Deleted equivalent with id: {}", id);
    }
}

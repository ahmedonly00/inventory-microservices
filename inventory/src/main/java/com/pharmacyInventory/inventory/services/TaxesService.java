package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.taxes.TaxesDTO;
import com.pharmacyInventory.inventory.mapper.TaxesMapper;
import com.pharmacyInventory.inventory.model.Taxes;
import com.pharmacyInventory.inventory.repository.TaxesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxesService {

    private final TaxesRepository taxesRepository;
    private final TaxesMapper taxesMapper;

    public List<TaxesDTO> getAllTaxes() {
        log.info("Fetching all taxes");
        
        try {
            List<Taxes> taxes = taxesRepository.findAll();
            log.info("Retrieved {} taxes", taxes.size());
            return taxesMapper.toTaxesDTO(taxes);
            
        } catch (Exception e) {
            log.error("Error fetching all taxes", e);
            throw new RuntimeException("Failed to fetch taxes: " + e.getMessage());
        }
    }

    public TaxesDTO getTaxById(@NonNull Long id) {
        log.info("Fetching tax with id: {}", id);
        
        try {
            Taxes tax = taxesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
            
            log.info("Found tax with id: {}", id);
            return taxesMapper.toTaxesDTO(tax);
            
        } catch (Exception e) {
            log.error("Error fetching tax with id: {}", id, e);
            throw new RuntimeException("Failed to fetch tax: " + e.getMessage());
        }
    }

    public TaxesDTO createTax(@NonNull TaxesDTO taxDTO) {
        log.info("Creating new tax: {}", taxDTO.getName());
        
        try {
            // Check if tax with same name already exists
            String taxName = taxDTO.getName();
            if (taxName != null && taxesRepository.findByName(taxName).isPresent()) {
                throw new RuntimeException("Tax with name '" + taxName + "' already exists");
            }

            Taxes tax = taxesMapper.toTaxes(taxDTO);
            tax.setCreatedAt(LocalDateTime.now());
            tax.setUpdatedAt(LocalDateTime.now());
            tax.setIsActive(taxDTO.getIsActive() != null ? taxDTO.getIsActive() : true);

            Taxes saved = taxesRepository.save(tax);
            log.info("Created tax with id: {}", saved.getId());
            return taxesMapper.toTaxesDTO(saved);
            
        } catch (Exception e) {
            log.error("Error creating tax", e);
            throw new RuntimeException("Failed to create tax: " + e.getMessage());
        }
    }

    public TaxesDTO updateTax(@NonNull Long id, @NonNull TaxesDTO taxDTO) {
        log.info("Updating tax with id: {}", id);
        
        try {
            Taxes existingTax = taxesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));

            // Update fields
            if (taxDTO.getName() != null) {
                // Check if new name conflicts with existing tax
                String newName = taxDTO.getName();
                if (newName != null && taxesRepository.findByName(newName).isPresent() && 
                    !taxesRepository.findByName(newName).get().getId().equals(id)) {
                    throw new RuntimeException("Tax with name '" + newName + "' already exists");
                }
                existingTax.setName(newName);
            }
            if (taxDTO.getDescription() != null) {
                existingTax.setDescription(taxDTO.getDescription());
            }
            if (taxDTO.getRate() != null) {
                existingTax.setRate(taxDTO.getRate());
            }
            if (taxDTO.getIsActive() != null) {
                existingTax.setIsActive(taxDTO.getIsActive());
            }
            
            existingTax.setUpdatedAt(LocalDateTime.now());
            
            Taxes updated = taxesRepository.save(existingTax);
            log.info("Updated tax with id: {}", id);
            return taxesMapper.toTaxesDTO(updated);
            
        } catch (Exception e) {
            log.error("Error updating tax with id: {}", id, e);
            throw new RuntimeException("Failed to update tax: " + e.getMessage());
        }
    }

    public TaxesDTO activateTax(@NonNull Long id) {
        log.info("Activating tax with id: {}", id);
        
        try {
            Taxes tax = taxesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
            
            tax.setIsActive(true);
            tax.setUpdatedAt(LocalDateTime.now());
            
            Taxes activated = taxesRepository.save(tax);
            log.info("Activated tax with id: {}", id);
            return taxesMapper.toTaxesDTO(activated);
            
        } catch (Exception e) {
            log.error("Error activating tax with id: {}", id, e);
            throw new RuntimeException("Failed to activate tax: " + e.getMessage());
        }
    }

    public TaxesDTO deactivateTax(@NonNull Long id) {
        log.info("Deactivating tax with id: {}", id);
        
        try {
            Taxes tax = taxesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
            
            tax.setIsActive(false);
            tax.setUpdatedAt(LocalDateTime.now());
            
            Taxes deactivated = taxesRepository.save(tax);
            log.info("Deactivated tax with id: {}", id);
            return taxesMapper.toTaxesDTO(deactivated);
            
        } catch (Exception e) {
            log.error("Error deactivating tax with id: {}", id, e);
            throw new RuntimeException("Failed to deactivate tax: " + e.getMessage());
        }
    }

    public void deleteTax(@NonNull Long id) {
        log.info("Deleting tax with id: {}", id);
        
        try {
            if (!taxesRepository.existsById(id)) {
                throw new RuntimeException("Tax not found with id: " + id);
            }
            
            taxesRepository.deleteById(id);
            log.info("Deleted tax with id: {}", id);
            
        } catch (Exception e) {
            log.error("Error deleting tax with id: {}", id, e);
            throw new RuntimeException("Failed to delete tax: " + e.getMessage());
        }
    }

    public List<TaxesDTO> getActiveTaxes() {
        log.info("Fetching active taxes only");
        
        try {
            List<Taxes> activeTaxes = taxesRepository.findByIsActive(true);
            log.info("Retrieved {} active taxes", activeTaxes.size());
            return taxesMapper.toTaxesDTO(activeTaxes);
            
        } catch (Exception e) {
            log.error("Error fetching active taxes", e);
            throw new RuntimeException("Failed to fetch active taxes: " + e.getMessage());
        }
    }
}
package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.mapper.SuppliersMapper;
import com.pharmacyInventory.inventory.model.Suppliers;
import com.pharmacyInventory.inventory.repository.SuppliersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuppliersService {

    private final SuppliersRepository suppliersRepository;
    private final SuppliersMapper suppliersMapper;

    public List<SuppliersDTO> getAllSuppliers() {
        log.info("Fetching all suppliers");
        List<Suppliers> suppliers = suppliersRepository.findAll();
        return suppliersMapper.toSuppliersDTO(suppliers);
    }

    public SuppliersDTO createSupplier(SuppliersDTO supplierDTO) {
        log.info("Creating new supplier: {}", supplierDTO.getName());
        
        // Check if supplier with same name already exists
        if (suppliersRepository.existsByName(supplierDTO.getName())) {
            throw new RuntimeException("Supplier with name '" + supplierDTO.getName() + "' already exists");
        }

        Suppliers supplier = suppliersMapper.toSuppliers(supplierDTO);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setIsActive(true);

        Suppliers saved = suppliersRepository.save(supplier);
        log.info("Created supplier with id: {}", saved.getId());
        return suppliersMapper.toSuppliersDTO(saved);
    }

    public SuppliersDTO updateSupplier(Long id, SuppliersDTO supplierDTO) {
        log.info("Updating supplier with id: {}", id);
        
        Suppliers existing = suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        // Update fields
        existing.setName(supplierDTO.getName());
        existing.setContactName(supplierDTO.getContactName());
        existing.setEmail(supplierDTO.getEmail());
        existing.setPhone(supplierDTO.getPhone());
        existing.setAddress(supplierDTO.getAddress());
        existing.setUpdatedAt(LocalDateTime.now());

        Suppliers updated = suppliersRepository.save(existing);
        log.info("Updated supplier with id: {}", id);
        return suppliersMapper.toSuppliersDTO(updated);
    }

    public void deactivateSupplier(Long id) {
        log.info("Deactivating supplier with id: {}", id);
        
        Suppliers supplier = suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        supplier.setIsActive(false);
        supplier.setUpdatedAt(LocalDateTime.now());

        suppliersRepository.save(supplier);
        log.info("Deactivated supplier with id: {}", id);
    }

    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with id: {}", id);
        
        Suppliers supplier = suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        suppliersRepository.delete(supplier);
        log.info("Deleted supplier with id: {}", id);
    }
}

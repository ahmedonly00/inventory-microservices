package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.exception.DuplicateResourceException;
import com.pharmacyInventory.inventory.exception.ResourceNotFoundException;
import com.pharmacyInventory.inventory.mapper.SuppliersMapper;
import com.pharmacyInventory.inventory.model.Suppliers;
import com.pharmacyInventory.inventory.repository.SuppliersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuppliersService {

    private final SuppliersRepository suppliersRepository;
    private final SuppliersMapper suppliersMapper;

    @Transactional(readOnly = true)
    public List<SuppliersDTO> getAllSuppliersByBranch(String branchId) {
        log.info("Fetching all suppliers for branch: {}", branchId);
        return suppliersRepository.findByBranchId(branchId).stream()
                .map(suppliersMapper::toSuppliersDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SuppliersDTO getSuppliersById(Long id, String branchId) {
        log.info("Fetching supplier with id: {} for branch: {}", id, branchId);
        return suppliersRepository.findByIdAndBranchId(id, branchId)
                .map(suppliersMapper::toSuppliersDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Supplier not found with id: " + id + " for branch: " + branchId));
    }

    @Transactional(readOnly = true)
    public SuppliersDTO createSupplier(String branchId, SuppliersDTO supplierDTO) {
        log.info("Creating new supplier: {} for branch: {}", supplierDTO.getName(), branchId);
        
        // Check if supplier with same name already exists
        if (suppliersRepository.existsByName(supplierDTO.getName())) {
            throw new RuntimeException("Supplier with name '" + supplierDTO.getName() + "' already exists");
        }

        if (suppliersRepository.existsByEmailAndBranchId(supplierDTO.getEmail(), branchId)) {
            throw new DuplicateResourceException("Email already exists in this branch");
        }

        Suppliers supplier = suppliersMapper.toSuppliers(supplierDTO);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setIsActive(true);
        supplier.setBranchId(branchId);

        Suppliers saved = suppliersRepository.save(supplier);
        log.info("Created supplier with id: {} for branch: {}", saved.getId(), branchId);
        return suppliersMapper.toSuppliersDTO(saved);
    }

    @Transactional
    public SuppliersDTO updateSupplier(Long id, SuppliersDTO supplierDTO, String branchId) {
        log.info("Updating supplier with id: {} for branch {}", id, branchId);
        
        Suppliers existing = suppliersRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id + " for branch: " + branchId));

        // Check for duplicate email
        if (!existing.getEmail().equalsIgnoreCase(supplierDTO.getEmail()) && 
            suppliersRepository.existsByEmailAndBranchId(supplierDTO.getEmail(), branchId)) {
            throw new DuplicateResourceException("Email already exists in this branch");
        }       

        // Update fields
        existing.setName(supplierDTO.getName());
        existing.setContactName(supplierDTO.getContactName());
        existing.setEmail(supplierDTO.getEmail());
        existing.setPhone(supplierDTO.getPhone());
        existing.setAddress(supplierDTO.getAddress());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setBranchId(branchId);

        Suppliers updated = suppliersRepository.save(existing);
        log.info("Updated supplier with id: {} for branch: {}", id, branchId);
        return suppliersMapper.toSuppliersDTO(updated);
    }

    @Transactional
    public void deactivateSupplier(Long id, String branchId) {
        log.info("Deactivating supplier with id: {} for branch: {}", id, branchId);
        
        Suppliers supplier = suppliersRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id + " for branch: " + branchId));

        supplier.setIsActive(false);
        supplier.setUpdatedAt(LocalDateTime.now());

        suppliersRepository.save(supplier);
        log.info("Deactivated supplier with id: {} for branch: {}", id, branchId);
    }

    @Transactional
    public void deleteSupplier(Long id, String branchId) {
        log.info("Deleting supplier with id: {} for branch: {}", id, branchId);
        
        Suppliers supplier = suppliersRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id + " for branch: " + branchId));

        suppliersRepository.delete(supplier);
        log.info("Deleted supplier with id: {} for branch: {}", id, branchId);
    }

    @Transactional(readOnly = true)    
    public List<SuppliersDTO> searchSuppliers(String query, String branchId) {
        log.info("Searching suppliers with query: {} for branch: {}", query, branchId);
        return suppliersRepository.searchByNameOrEmailOrPhone(query, branchId).stream()
                .map(suppliersMapper::toSuppliersDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SuppliersDTO> getActiveSuppliers(String branchId) {
        log.info("Getting active suppliers for branch: {}", branchId);
        return suppliersRepository.findByIsActiveTrueAndBranchId(branchId).stream()
                .map(suppliersMapper::toSuppliersDTO)
                .collect(Collectors.toList());
    }
        
    @Transactional(readOnly = true)
    public List<SuppliersDTO> getInactiveSuppliers(String branchId) {
        log.info("Getting inactive suppliers for branch: {}", branchId);
        return suppliersRepository.findByIsActiveFalseAndBranchId(branchId).stream()
                .map(suppliersMapper::toSuppliersDTO)
                .collect(Collectors.toList());
    }
        
    @Transactional(readOnly = true)
    public Map<String, Long> getSupplierCounts(String branchId) {
        log.info("Getting supplier counts for branch: {}", branchId);
        Map<String, Long> counts = new HashMap<>();
        counts.put("total", (long) suppliersRepository.findByBranchId(branchId).size());
        counts.put("active", (long) suppliersRepository.findByIsActiveTrueAndBranchId(branchId).stream().count());
        counts.put("inactive", (long) suppliersRepository.findByIsActiveFalseAndBranchId(branchId).stream().count());
        return counts;
    }
    

    @Transactional(readOnly = true)
    public SuppliersDTO getSupplierByBranch(String branchId, Pageable pageable) {
        log.info("Getting supplier for branch: {}", branchId);
        Suppliers supplier = suppliersRepository.findByBranchId(branchId, pageable)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Supplier not found for branch: " + branchId));
        return suppliersMapper.toSuppliersDTO(supplier);
    }

}

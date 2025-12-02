package com.pharmacyInventory.inventory.mapper;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.model.Suppliers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SuppliersMapper {

    public List<SuppliersDTO> toSuppliersDTO(List<Suppliers> suppliers) {
        if (suppliers == null) {
            return null;
        }
        return suppliers.stream()
                .map(this::toSuppliersDTO)
                .collect(Collectors.toList());
    }

    public SuppliersDTO toSuppliersDTO(Suppliers supplier) {
        if(supplier == null){
            return null;
        }

        return SuppliersDTO.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactName(supplier.getContactName())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .isActive(supplier.getIsActive())
                .createdAt(supplier.getCreatedAt())
                .medicationIds(supplier.getMedications() != null ? 
                    supplier.getMedications().stream().map(m -> m.getMedicationId()).collect(Collectors.toList()) : null)
                .build();
    }

    public Suppliers toSuppliers(SuppliersDTO suppliersDTO) {
        if(suppliersDTO == null) {
            return null;
        }

        Suppliers supplier = new Suppliers();
        supplier.setId(suppliersDTO.getId());
        supplier.setName(suppliersDTO.getName());
        supplier.setContactName(suppliersDTO.getContactName());
        supplier.setEmail(suppliersDTO.getEmail());
        supplier.setPhone(suppliersDTO.getPhone());
        supplier.setAddress(suppliersDTO.getAddress());
        supplier.setIsActive(suppliersDTO.getIsActive());
        supplier.setCreatedAt(suppliersDTO.getCreatedAt());
                
        return supplier;
    } 
    
}

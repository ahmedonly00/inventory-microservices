package com.pharmacyInventory.inventory.mapper;

import com.pharmacyInventory.inventory.dtos.taxes.TaxesDTO;
import com.pharmacyInventory.inventory.model.Taxes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaxesMapper {

    public List<TaxesDTO> toTaxesDTO(List<Taxes> taxes) {
        if (taxes == null) {
            return null;
        }
        return taxes.stream()
                .map(this::toTaxesDTO)
                .collect(Collectors.toList());
    }

    public TaxesDTO toTaxesDTO(Taxes tax) {
        if(tax == null){
            return null;
        }

        return TaxesDTO.builder()
                .id(tax.getId())
                .branchId(tax.getBranchId())
                .name(tax.getName())
                .rate(tax.getRate())
                .isActive(tax.getIsActive())
                .description(tax.getDescription())
                .createdAt(tax.getCreatedAt())
                .updatedAt(tax.getUpdatedAt())
                .salesIds(tax.getSales() != null ? 
                    tax.getSales().stream().map(s -> s.getId()).collect(Collectors.toList()) : null)
                .build();
    }

    public Taxes toTaxes(TaxesDTO taxesDTO) {
        if(taxesDTO == null) {
            return null;
        }

        Taxes tax = new Taxes();
        tax.setId(taxesDTO.getId());
        tax.setBranchId(taxesDTO.getBranchId());
        tax.setName(taxesDTO.getName());
        tax.setRate(taxesDTO.getRate());
        tax.setIsActive(taxesDTO.getIsActive());
        tax.setDescription(taxesDTO.getDescription());
        tax.setCreatedAt(taxesDTO.getCreatedAt());
        tax.setUpdatedAt(taxesDTO.getUpdatedAt());
                
        return tax;
    } 
    
}

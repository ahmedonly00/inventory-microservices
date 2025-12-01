package com.pharmacyInventory.salesService.mapper;

import com.pharmacyInventory.salesService.dtos.sales.SaleItemDTO;
import com.pharmacyInventory.salesService.model.SaleItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaleItemMapper {

    public List<SaleItemDTO> toSaleItemDTO(List<SaleItem> saleItems) {
        if (saleItems == null) {
            return null;
        }
        return saleItems.stream()
                .map(this::toSaleItemDTO)
                .collect(Collectors.toList());
    }

    public SaleItemDTO toSaleItemDTO(SaleItem saleItem) {
        if(saleItem == null){
            return null;
        }

        return SaleItemDTO.builder()
                .id(saleItem.getId())
                .quantity(saleItem.getQuantity())
                .unitPrice(saleItem.getUnitPrice())
                .subTotal(saleItem.getSubTotal())
                .saleId(saleItem.getSales() != null ? saleItem.getSales().getId() : null)
                .medicationId(saleItem.getMedication() != null ? saleItem.getMedication().getMedicationId() : null)
                .medicationName(saleItem.getMedication() != null ? saleItem.getMedication().getName() : null)
                .build();
    }

    public SaleItem toSaleItem(SaleItemDTO saleItemDTO) {
        if(saleItemDTO == null) {
            return null;
        }

        SaleItem saleItem = new SaleItem();
        saleItem.setId(saleItemDTO.getId());
        saleItem.setQuantity(saleItemDTO.getQuantity());
        saleItem.setUnitPrice(saleItemDTO.getUnitPrice());
        saleItem.setSubTotal(saleItemDTO.getSubTotal());
        
                
        return saleItem;
    } 
    
}

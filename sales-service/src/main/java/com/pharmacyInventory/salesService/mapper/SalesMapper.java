package com.pharmacyInventory.salesService.mapper;

import com.pharmacyInventory.salesService.dtos.sales.SalesDTO;
import com.pharmacyInventory.salesService.model.Sales;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesMapper {

    public List<SalesDTO> toSalesDTO(List<Sales> sales) {
        if (sales == null) {
            return null;
        }
        return sales.stream()
                .map(this::toSalesDTO)
                .collect(Collectors.toList());
    }

    public SalesDTO toSalesDTO(Sales sale) {
        if(sale == null){
            return null;
        }

        return SalesDTO.builder()
                .id(sale.getId())
                .totalAmount(sale.getTotalAmount())
                .taxAmount(sale.getTaxAmount())
                .paymentMethod(sale.getPaymentMethod())
                .discount(sale.getDiscount())
                .saleDate(sale.getSaleDate())
                .customerName(sale.getCustomerName())
                .customerPhone(sale.getCustomerPhone())
                .userId(sale.getUser() != null ? sale.getUser().getId() : null)
                .userName(sale.getUser() != null ? sale.getUser().getName() : null)
                .saleItemIds(sale.getSaleItems() != null ? 
                    sale.getSaleItems().stream().map(si -> si.getId()).collect(Collectors.toList()) : null)
                .taxIds(sale.getTaxes() != null ? 
                    sale.getTaxes().stream().map(t -> t.getId()).collect(Collectors.toList()) : null)
                .build();
    }

    public Sales toSales(SalesDTO salesDTO) {
        if(salesDTO == null) {
            return null;
        }

        Sales sale = new Sales();
        sale.setId(salesDTO.getId());
        sale.setTotalAmount(salesDTO.getTotalAmount());
        sale.setTaxAmount(salesDTO.getTaxAmount());
        sale.setPaymentMethod(salesDTO.getPaymentMethod());
        sale.setDiscount(salesDTO.getDiscount());
        sale.setSaleDate(salesDTO.getSaleDate());
        sale.setCustomerName(salesDTO.getCustomerName());
        sale.setCustomerPhone(salesDTO.getCustomerPhone());
                
        return sale;
    } 
    
}

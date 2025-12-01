package com.inventory.pharmacyInventory.mapper;

import com.inventory.pharmacyInventory.dtos.stock.StockDTO;
import com.inventory.pharmacyInventory.model.Stock;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockMapper {

    public List<StockDTO> toStockDTO(List<Stock> stocks) {
        if (stocks == null) {
            return null;
        }
        return stocks.stream()
                .map(this::toStockDTO)
                .collect(Collectors.toList());
    }

    public StockDTO toStockDTO(Stock stock) {
        if(stock == null){
            return null;
        }

        return StockDTO.builder()
                .id(stock.getId())
                .quantity(stock.getQuantity())
                .batchNumber(stock.getBatchNumber())
                .expiryDate(stock.getExpiryDate())
                .build();
    }

    public Stock toStock(StockDTO stockDTO) {
        if(stockDTO == null) {
            return null;
        }

        Stock stock = new Stock();
        stock.setId(stockDTO.getId());
        stock.setQuantity(stockDTO.getQuantity());
        stock.setBatchNumber(stockDTO.getBatchNumber());
        stock.setExpiryDate(stockDTO.getExpiryDate());
                
        return stock;
    } 
    
}

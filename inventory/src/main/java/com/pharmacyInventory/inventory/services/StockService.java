package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.model.Stock;
import com.pharmacyInventory.inventory.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    
    private final StockRepository stockRepository;
    
    
    public List<Stock> getAllStocks(Long branchId) {
        log.info("Fetching all stocks for branch with id: {}", branchId);
        return stockRepository.findAll();
    }
    
    public Optional<Stock> getStockById(Long id) {
        log.info("Fetching stock with id: {}", id);
        return stockRepository.findById(id);
    }
    
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
    
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
    
}

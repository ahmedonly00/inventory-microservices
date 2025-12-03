package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.repository.MedicationsRepository;
import com.pharmacyInventory.inventory.repository.PurchaseRepository;
import com.pharmacyInventory.inventory.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final MedicationsRepository medicationsRepository;
    private final SalesRepository salesRepository;
    private final PurchaseRepository purchaseRepository;
                              

    public Map<String, Object> getDashboardStats() {
        log.info("Calculating dashboard statistics");
        
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        try {
            LocalDate today = LocalDate.now();            
            // 1. Basic Metrics
            calculateBasicMetrics(data);
            
            // 2. Sales vs Purchases Trend (Last 12 months)
            calculateSalesPurchasesTrend(data, today);
            
            // 3. Revenue by Payment Method
            calculateRevenueByPaymentMethod(data);
            
            // 4. Top 10 Selling Medications
            calculateTopSellingMedications(data);
            
            // 5. Stock Level Distribution
            calculateStockLevelDistribution(data);
            
            // 6. Expiry Forecast
            calculateExpiryForecast(data, today);
            
            response.put("success", true);
            response.put("data", data);
            
            log.info("Dashboard statistics calculated successfully");
            
        } catch (Exception e) {
            log.error("Error calculating dashboard statistics", e);
            response.put("success", false);
            response.put("message", "Failed to calculate dashboard statistics: " + e.getMessage());
        }
        
        return response;
    }
    
    private void calculateBasicMetrics(Map<String, Object> data) {
        // Total Sales (sum of all sales)
        double totalSales = salesRepository.findAll().stream()
                .mapToDouble(sale -> sale.getTotalAmount() != null ? sale.getTotalAmount() : 0.0)
                .sum();
        data.put("totalSales", roundToTwoDecimalPlaces(totalSales));
        
        // Total Purchases (sum of all purchases)
        double totalPurchases = purchaseRepository.findAll().stream()
                .mapToDouble(purchase -> purchase.getTotalAmount() != null ? purchase.getTotalAmount() : 0.0)
                .sum();
        data.put("totalPurchases", roundToTwoDecimalPlaces(totalPurchases));
        
        // Stock Value (sum of (quantity * price) for all medications)
        double stockValue = medicationsRepository.findAll().stream()
                .mapToDouble(med -> {
                    double price = med.getPrice() != null ? med.getPrice() : 0.0;
                    int quantity = med.getStockQuantity() != null ? med.getStockQuantity() : 0;
                    return price * quantity;
                })
                .sum();
        data.put("stockValue", roundToTwoDecimalPlaces(stockValue));
        
        // Expiry Loss (sum of value of expired medications)
        double expiryLoss = medicationsRepository.findAll().stream()
                .filter(med -> med.getExpiryDate() != null && med.getExpiryDate().isBefore(LocalDate.now()))
                .mapToDouble(med -> {
                    double price = med.getPrice() != null ? med.getPrice() : 0.0;
                    int quantity = med.getStockQuantity() != null ? med.getStockQuantity() : 0;
                    return price * quantity;
                })
                .sum();
        data.put("expiryLoss", roundToTwoDecimalPlaces(expiryLoss));
        
        // Low Stock Items (items below reorder level)
        long lowStockItems = medicationsRepository.findAll().stream()
                .filter(med -> med.getStockQuantity() != null && med.getReorderLevel() != null)
                .filter(med -> med.getStockQuantity() <= med.getReorderLevel())
                .count();
        data.put("lowStockItems", lowStockItems);
        
        // Expiring Items (within 30 days)
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        long expiringItems = medicationsRepository.findAll().stream()
                .filter(med -> med.getExpiryDate() != null)
                .filter(med -> (med.getExpiryDate().isAfter(LocalDate.now()) || med.getExpiryDate().isEqual(LocalDate.now())) 
                        && (med.getExpiryDate().isBefore(thirtyDaysFromNow) || med.getExpiryDate().isEqual(thirtyDaysFromNow)))
                .count();
        data.put("expiringItems", expiringItems);
    }
    
    private void calculateSalesPurchasesTrend(Map<String, Object> data, LocalDate today) {
        List<Map<String, Object>> monthlyTrend = new ArrayList<>();
        
        // Get data for the last 12 months
        for (int i = 11; i >= 0; i--) {
            LocalDate startDate = today.minusMonths(i).withDayOfMonth(1);
            LocalDate endDate = today.minusMonths(i-1).withDayOfMonth(1).minusDays(1);
            
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            
            // Calculate monthly sales
            double monthlySales = salesRepository.findBySaleDateBetween(startDateTime, endDateTime).stream()
                    .mapToDouble(sale -> sale.getTotalAmount() != null ? sale.getTotalAmount() : 0.0)
                    .sum();
            
            // Calculate monthly purchases
            double monthlyPurchases = purchaseRepository.findByPurchaseDateBetween(startDateTime, endDateTime).stream()
                    .mapToDouble(purchase -> purchase.getTotalAmount() != null ? purchase.getTotalAmount() : 0.0)
                    .sum();
            
            Map<String, Object> monthlyData = new HashMap<>();
            monthlyData.put("month", startDate.getMonth().toString() + " " + startDate.getYear());
            monthlyData.put("sales", roundToTwoDecimalPlaces(monthlySales));
            monthlyData.put("purchases", roundToTwoDecimalPlaces(monthlyPurchases));
            monthlyTrend.add(monthlyData);
        }
        data.put("monthlyTrend", monthlyTrend);
    }
    
    private void calculateRevenueByPaymentMethod(Map<String, Object> data) {
        Map<String, Double> revenueByPaymentMethod = new HashMap<>();
        
        salesRepository.findAll().forEach(sale -> {
            if (sale.getPaymentMethod() != null) {
                String method = sale.getPaymentMethod().toString();
                double amount = sale.getTotalAmount() != null ? sale.getTotalAmount() : 0.0;
                revenueByPaymentMethod.merge(method, amount, (v1, v2) -> v1 + v2);
            }
        });
        
        // Convert to list of maps for better JSON structure
        List<Map<String, Object>> paymentMethodData = revenueByPaymentMethod.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("method", entry.getKey());
                    item.put("amount", roundToTwoDecimalPlaces(entry.getValue()));
                    return item;
                })
                .collect(Collectors.toList());
                
        data.put("revenueByPaymentMethod", paymentMethodData);
    }
    
    private void calculateTopSellingMedications(Map<String, Object> data) {
        // This is a simplified version - in a real app, you'd want to query SaleItem for accurate data
        List<Map<String, Object>> topSelling = medicationsRepository.findAll().stream()
                .filter(med -> med.getSaleItems() != null && !med.getSaleItems().isEmpty())
                .map(med -> {
                    double totalSold = med.getSaleItems() != null ? med.getSaleItems().stream()
                            .filter(Objects::nonNull)
                            .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                            .sum() : 0.0;
                    
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", med.getMedicationId());
                    item.put("name", med.getName());
                    item.put("totalSold", roundToTwoDecimalPlaces(totalSold));
                    item.put("stockQuantity", med.getStockQuantity());
                    return item;
                })
                .sorted((a, b) -> Double.compare((Double)b.get("totalSold"), (Double)a.get("totalSold")))
                .limit(10)
                .collect(Collectors.toList());
                
        data.put("topSellingMedications", topSelling);
    }
    
    private void calculateStockLevelDistribution(Map<String, Object> data) {
        List<Medications> allMedications = medicationsRepository.findAll();
        
        Map<String, Object> stockLevels = new HashMap<>();
        
        // Count medications by stock level
        long outOfStock = allMedications.stream()
                .filter(med -> med.getStockQuantity() != null && med.getStockQuantity() == 0)
                .count();
                
        long lowStock = allMedications.stream()
                .filter(med -> med.getStockQuantity() != null && med.getReorderLevel() != null)
                .filter(med -> med.getStockQuantity() > 0 && med.getStockQuantity() <= med.getReorderLevel())
                .count();
                
        long inStock = allMedications.stream()
                .filter(med -> med.getStockQuantity() != null && med.getReorderLevel() != null)
                .filter(med -> med.getStockQuantity() > med.getReorderLevel())
                .count();
        
        stockLevels.put("outOfStock", outOfStock);
        stockLevels.put("lowStock", lowStock);
        stockLevels.put("inStock", inStock);
        
        data.put("stockLevelDistribution", stockLevels);
    }
    
    private void calculateExpiryForecast(Map<String, Object> data, LocalDate today) {
        Map<String, Object> expiryForecast = new HashMap<>();
        
        // Count medications expiring in 30, 60, and 90 days
        long expiringIn30Days = countExpiringMedications(today, 30);
        long expiringIn60Days = countExpiringMedications(today, 60);
        long expiringIn90Days = countExpiringMedications(today, 90);
        
        // Get list of medications expiring soon
        LocalDate endDate30 = today.plusDays(30);
        List<Map<String, Object>> soonToExpire = medicationsRepository.findAll().stream()
                .filter(med -> med.getExpiryDate() != null && 
                        !med.getExpiryDate().isBefore(today) && 
                        !med.getExpiryDate().isAfter(endDate30))
                .map(med -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", med.getMedicationId());
                    item.put("name", med.getName());
                    item.put("expiryDate", med.getExpiryDate().toString());
                    item.put("daysToExpire", ChronoUnit.DAYS.between(today, med.getExpiryDate()));
                    item.put("stockQuantity", med.getStockQuantity());
                    return item;
                })
                .sorted(Comparator.comparing(item -> (Long)item.get("daysToExpire")))
                .collect(Collectors.toList());
        
        expiryForecast.put("expiringIn30Days", expiringIn30Days);
        expiryForecast.put("expiringIn60Days", expiringIn60Days);
        expiryForecast.put("expiringIn90Days", expiringIn90Days);
        expiryForecast.put("soonToExpireList", soonToExpire);
        
        data.put("expiryForecast", expiryForecast);
    }
    
    private long countExpiringMedications(LocalDate startDate, int days) {
        LocalDate endDate = startDate.plusDays(days);
        return medicationsRepository.findAll().stream()
                .filter(med -> med.getExpiryDate() != null)
                .filter(med -> !med.getExpiryDate().isBefore(startDate) && !med.getExpiryDate().isAfter(endDate))
                .count();
    }
    
    private double roundToTwoDecimalPlaces(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    }

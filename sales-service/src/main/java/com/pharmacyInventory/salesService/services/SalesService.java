package com.inventory.pharmacyInventory.services;

import com.inventory.pharmacyInventory.dtos.sales.SalesDTO;
import com.inventory.pharmacyInventory.dtos.notifications.NotificationsDTO;
import com.inventory.pharmacyInventory.mapper.SalesMapper;
import com.inventory.pharmacyInventory.model.Medications;
import com.inventory.pharmacyInventory.model.SaleItem;
import com.inventory.pharmacyInventory.model.Sales;
import com.inventory.pharmacyInventory.model.Users;
import com.inventory.pharmacyInventory.Enum.Roles;
import com.inventory.pharmacyInventory.Enum.StockStatus;
import com.inventory.pharmacyInventory.repository.MedicationsRepository;
import com.inventory.pharmacyInventory.repository.SalesRepository;
import com.inventory.pharmacyInventory.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesService {

    private final SalesRepository salesRepository;
    private final SalesMapper salesMapper;
    private final MedicationsRepository medicationsRepository;
    private final UsersRepository usersRepository;
    private final NotificationsService notificationsService;

    public List<SalesDTO> getAllSales() {
        log.info("Fetching all sales");
        List<Sales> sales = salesRepository.findAll();
        return salesMapper.toSalesDTO(sales);
    }

    public SalesDTO getSaleById(Long id) {
        log.info("Fetching sale with id: {}", id);
        Sales sale = salesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
        return salesMapper.toSalesDTO(sale);
    }

    public SalesDTO createSale(SalesDTO salesDTO) {
        log.info("Creating new sale");
        
        Sales sale = salesMapper.toSales(salesDTO);
        sale.setSaleDate(LocalDateTime.now());

        // Update medication stock quantities
        updateMedicationStock(sale);

        Sales saved = salesRepository.save(sale);
        log.info("Created sale with id: {}", saved.getId());
        return salesMapper.toSalesDTO(saved);
    }

    private void updateMedicationStock(Sales sale) {
        log.info("Updating medication stock for sale id: {}", sale.getId());
        
        if (sale.getSaleItems() == null || sale.getSaleItems().isEmpty()) {
            log.warn("No sale items found for sale id: {}", sale.getId());
            return;
        }
        
        for (SaleItem saleItem : sale.getSaleItems()) {
            try {
                Medications medication = saleItem.getMedication();
                if (medication == null) {
                    log.error("Medication not found for sale item id: {}", saleItem.getId());
                    continue;
                }
                
                Integer quantitySold = saleItem.getQuantity();
                Integer currentStock = medication.getStockQuantity();
                
                if (currentStock == null) {
                    log.error("Current stock is null for medication id: {}", medication.getId());
                    throw new RuntimeException("Stock quantity is not set for medication: " + medication.getName());
                }
                
                if (currentStock < quantitySold) {
                    log.error("Insufficient stock for medication: {}. Required: {}, Available: {}", 
                            medication.getName(), quantitySold, currentStock);
                    throw new RuntimeException("Insufficient stock for medication: " + medication.getName() + 
                            ". Required: " + quantitySold + ", Available: " + currentStock);
                }
                
                // Update stock quantity
                Integer newStock = currentStock - quantitySold;
                medication.setStockQuantity(newStock);
                
                // Save the updated medication
                medicationsRepository.save(medication);
                
                log.info("Updated stock for medication: {} (ID: {}). Old stock: {}, New stock: {}, Sold: {}", 
                        medication.getName(), medication.getId(), currentStock, newStock, quantitySold);
                
                // Check for low stock warning and send notifications
                if (newStock <= medication.getReorderLevel()) {
                    log.warn("Low stock alert for medication: {} (ID: {}). Current stock: {}, Reorder level: {}", 
                            medication.getName(), medication.getId(), newStock, medication.getReorderLevel());
                    
                    sendLowStockNotification(medication, newStock);
                }
                
            } catch (Exception e) {
                log.error("Error updating stock for sale item id: {}", saleItem.getId(), e);
                throw new RuntimeException("Failed to update stock for medication: " + e.getMessage());
            }
        }
    }
    
    private void sendLowStockNotification(Medications medication, Integer currentStock) {
        try {
            // Find all users with MANAGER or ADMIN roles
            List<Users> managersAndAdmins = usersRepository.findAll().stream()
                    .filter(user -> (user.getRole() == Roles.MANAGER || user.getRole() == Roles.ADMIN) 
                            && user.getIsActive())
                    .toList();
            
            if (managersAndAdmins.isEmpty()) {
                log.warn("No active managers or admins found to send low stock notification");
                return;
            }
            
            // Create notification message
            String message = String.format(
                "LOW STOCK ALERT: Medication '%s' (Batch: %s) is running low on stock. Current stock: %d, Reorder level: %d. Please reorder soon.",
                medication.getName(),
                medication.getBatchNumber(),
                currentStock,
                medication.getReorderLevel()
            );
            
            // Send notification to each manager/admin
            for (Users user : managersAndAdmins) {
                try {
                    NotificationsDTO notification = NotificationsDTO.builder()
                            .userId(user.getId())
                            .message(message)
                            .type(StockStatus.LOW_STOCK.name())
                            .isRead(false)
                            .build();
                    
                    notificationsService.createNotification(notification);
                    log.info("Low stock notification sent to user: {} ({})", user.getName(), user.getEmail());
                    
                } catch (Exception e) {
                    log.error("Failed to send low stock notification to user: {} ({})", user.getName(), user.getEmail(), e);
                }
            }
            
            log.info("Low stock notifications sent for medication: {} to {} managers/admins", 
                    medication.getName(), managersAndAdmins.size());
                    
        } catch (Exception e) {
            log.error("Error sending low stock notifications for medication: {}", medication.getName(), e);
            // Don't throw exception here as it shouldn't break the sale process
        }
    }
}

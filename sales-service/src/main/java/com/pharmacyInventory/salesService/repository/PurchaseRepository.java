package com.pharmacyInventory.salesService.repository;

import com.pharmacyInventory.salesService.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    /**
     * Find all purchases within a date range
     * @param startDate Start date of the range (inclusive)
     * @param endDate End date of the range (inclusive)
     * @return List of purchases within the date range
     */
    List<Purchase> findByPurchaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find all purchases by supplier ID
     * @param supplierId The ID of the supplier
     * @return List of purchases for the given supplier
     */
    List<Purchase> findBySupplierId(Long supplierId);
    
    /**
     * Find all purchases by user ID
     * @param userId The ID of the user who made the purchase
     * @return List of purchases made by the user
     */
    List<Purchase> findByUserId(Long userId);
    
    /**
     * Find purchases by invoice number (exact match)
     * @param invoiceNumber The invoice number to search for
     * @return List of purchases with the given invoice number
     */
    List<Purchase> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Find purchases containing the given notes (case-insensitive)
     * @param notes The text to search for in purchase notes
     * @return List of purchases containing the given text in notes
     */
    List<Purchase> findByNotesContainingIgnoreCase(String notes);
    
    /**
     * Find all purchases ordered by purchase date in descending order
     * @return List of purchases ordered by purchase date (newest first)
     */
    List<Purchase> findAllByOrderByPurchaseDateDesc();
    
    /**
     * Check if a purchase with the given invoice number exists
     * @param invoiceNumber The invoice number to check
     * @return true if a purchase with the invoice number exists, false otherwise
     */
    boolean existsByInvoiceNumber(String invoiceNumber);
}

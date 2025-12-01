package com.pharmacyInventory.salesService.mapper;

import com.pharmacyInventory.salesService.dtos.transactions.TransactionsDTO;
import com.pharmacyInventory.salesService.model.Transactions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionsMapper {

    public List<TransactionsDTO> toTransactionsDTO(List<Transactions> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream()
                .map(this::toTransactionsDTO)
                .collect(Collectors.toList());
    }

    public TransactionsDTO toTransactionsDTO(Transactions transaction) {
        if(transaction == null){
            return null;
        }

        return TransactionsDTO.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .amount(transaction.getAmount())
                .reference(transaction.getReference())
                .notes(transaction.getNotes())
                .transactionType(transaction.getTransactionType())
                .createdBy(transaction.getCreatedBy())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .medicationId(transaction.getMedication() != null ? transaction.getMedication().getMedicationId() : null)
                .medicationName(transaction.getMedication() != null ? transaction.getMedication().getName() : null)
                .supplierId(transaction.getSuppliers() != null ? transaction.getSuppliers().getId() : null)
                .supplierName(transaction.getSuppliers() != null ? transaction.getSuppliers().getName() : null)
                .userId(transaction.getUser() != null ? transaction.getUser().getId() : null)
                .userName(transaction.getUser() != null ? transaction.getUser().getName() : null)
                .build();
    }

    public Transactions toTransactions(TransactionsDTO transactionsDTO) {
        if(transactionsDTO == null) {
            return null;
        }

        Transactions transaction = new Transactions();
        transaction.setId(transactionsDTO.getId());
        transaction.setType(transactionsDTO.getType());
        transaction.setQuantity(transactionsDTO.getQuantity());
        transaction.setAmount(transactionsDTO.getAmount());
        transaction.setReference(transactionsDTO.getReference());
        transaction.setNotes(transactionsDTO.getNotes());
        transaction.setTransactionType(transactionsDTO.getTransactionType());
        transaction.setCreatedBy(transactionsDTO.getCreatedBy());
        transaction.setCreatedAt(transactionsDTO.getCreatedAt());
        transaction.setUpdatedAt(transactionsDTO.getUpdatedAt());
                
        return transaction;
    } 
    
}

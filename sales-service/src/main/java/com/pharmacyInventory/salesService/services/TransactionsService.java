package com.inventory.pharmacyInventory.services;

import com.inventory.pharmacyInventory.dtos.transactions.TransactionsDTO;
import com.inventory.pharmacyInventory.mapper.TransactionsMapper;
import com.inventory.pharmacyInventory.model.Transactions;
import com.inventory.pharmacyInventory.repository.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final TransactionsMapper transactionsMapper;

    public List<TransactionsDTO> getAllTransactions() {
        log.info("Fetching all transactions");
        List<Transactions> transactions = transactionsRepository.findAll();
        return transactionsMapper.toTransactionsDTO(transactions);
    }

    public TransactionsDTO getTransactionById(Long id) {
        log.info("Fetching transaction with id: {}", id);
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return transactionsMapper.toTransactionsDTO(transaction);
    }

    public TransactionsDTO createTransaction(TransactionsDTO transactionDTO) {
        log.info("Creating new transaction: {} - {}", transactionDTO.getType(), transactionDTO.getMedicationName());

        Transactions transaction = transactionsMapper.toTransactions(transactionDTO);
        transaction.setCreatedAt(LocalDateTime.now());

        Transactions saved = transactionsRepository.save(transaction);
        log.info("Created transaction with id: {}", saved.getId());
        return transactionsMapper.toTransactionsDTO(saved);
    }

    public List<TransactionsDTO> getTransactionsByMedication(Long medicationId) {
        log.info("Fetching transactions for medication with id: {}", medicationId);
        List<Transactions> transactions = transactionsRepository.findByMedicationId(medicationId);
        return transactionsMapper.toTransactionsDTO(transactions);
    }

    public List<TransactionsDTO> getTransactionsByDateRange(String startDate, String endDate) {
        log.info("Fetching transactions between {} and {}", startDate, endDate);
        
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        List<Transactions> transactions = transactionsRepository.findByCreatedAtBetween(start, end);
        return transactionsMapper.toTransactionsDTO(transactions);
    }
}

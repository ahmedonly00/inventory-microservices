package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.transfers.TransfersDTO;
import com.pharmacyInventory.inventory.mapper.TransfersMapper;
import com.pharmacyInventory.inventory.model.Transfers;
import com.pharmacyInventory.inventory.repository.TransfersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransfersService {

    private final TransfersRepository transfersRepository;
    private final TransfersMapper transfersMapper;

    public List<TransfersDTO> getAllTransfers() {
        log.info("Fetching all transfers");
        List<Transfers> transfers = transfersRepository.findAll();
        return transfersMapper.toTransfersDTO(transfers);
    }

    public TransfersDTO getTransferById(Long id) {
        log.info("Fetching transfer with id: {}", id);
        Transfers transfer = transfersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found with id: " + id));
        return transfersMapper.toTransfersDTO(transfer);
    }

    public TransfersDTO createTransfer(TransfersDTO transferDTO) {
        log.info("Creating new transfer: {} from branch {} to branch {}", 
                transferDTO.getMedicationName(), transferDTO.getFromBranchId(), transferDTO.getToBranchId());

        Transfers transfer = transfersMapper.toTransfers(transferDTO);
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("PENDING");

        Transfers saved = transfersRepository.save(transfer);
        log.info("Created transfer with id: {}", saved.getId());
        return transfersMapper.toTransfersDTO(saved);
    }

    public TransfersDTO updateTransferStatus(Long id, String status) {
        log.info("Updating status for transfer with id: {} to {}", id, status);
        
        Transfers existing = transfersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found with id: " + id));

        existing.setStatus(status);
        existing.setUpdatedAt(LocalDateTime.now());

        Transfers updated = transfersRepository.save(existing);
        log.info("Updated transfer status with id: {}", id);
        return transfersMapper.toTransfersDTO(updated);
    }

    public List<TransfersDTO> getTransfersByBranch(Long branchId) {
        log.info("Fetching transfers for branch with id: {}", branchId);
        List<Transfers> transfers = transfersRepository.findByFromBranchIdOrToBranchId(branchId, branchId);
        return transfersMapper.toTransfersDTO(transfers);
    }
}

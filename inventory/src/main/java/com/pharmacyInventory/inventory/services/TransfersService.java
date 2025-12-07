package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.transfers.TransfersDTO;
import com.pharmacyInventory.inventory.mapper.TransfersMapper;
import com.pharmacyInventory.inventory.model.Transfers;
import com.pharmacyInventory.inventory.repository.TransfersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransfersService {

    private final TransfersRepository transfersRepository;
    private final TransfersMapper transfersMapper;

    public List<TransfersDTO> getAllTransfers(String branchId) {
        log.info("Fetching all transfers");
        List<Transfers> transfers = transfersRepository.findAllByBranchId(branchId);
        return transfersMapper.toTransfersDTO(transfers);
    }

    public TransfersDTO getTransferById(Long id, String branchId) {
        log.info("Fetching transfer with id: {}", id);
        Transfers transfer = transfersRepository.findByIdOrBranchId(id, branchId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transfer not found with id: " + id));
        return transfersMapper.toTransfersDTO(transfer);
    }

    public TransfersDTO createTransfer(TransfersDTO transferDTO, String branchId) {
        log.info("Creating new transfer: {} from branch {} to branch {}", 
                transferDTO.getMedicationName(), transferDTO.getBranchId(), transferDTO.getToBranchId());

        Transfers transfer = transfersMapper.toTransfers(transferDTO);
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setStatus("PENDING");

        transfer.setBranchId(transferDTO.getBranchId());
        transfer.setToBranchId(transferDTO.getToBranchId());
        Transfers saved = transfersRepository.save(transfer);
        log.info("Created transfer with id: {}", saved.getId());
        return transfersMapper.toTransfersDTO(saved);
    }

    public TransfersDTO updateTransferStatus(Long id, String status, TransfersDTO transferDTO, String branchId) {
        log.info("Updating status for transfer with id: {} to {}", id, status);
        
        Transfers existing = transfersRepository.findByIdOrBranchId(id, branchId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transfer not found with id: " + id));

        existing.setStatus(status);
        existing.setNotes(transferDTO.getNotes());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setBranchId(transferDTO.getBranchId());
        existing.setToBranchId(transferDTO.getToBranchId());

        Transfers updated = transfersRepository.save(existing);
        log.info("Updated transfer status with id: {}", id);
        return transfersMapper.toTransfersDTO(updated);
    }

    public List<TransfersDTO> getTransfersByBranch(String branchId, String toBranchId) {
        log.info("Fetching transfers for branch with id: {}", branchId);
        List<Transfers> transfers = transfersRepository.findByBranchIdOrToBranchId(branchId, toBranchId);
        return transfersMapper.toTransfersDTO(transfers);   
    }

    public List<TransfersDTO> searchTransfersByIdorByBranchId(Long id, String branchId) {
        log.info("Fetching transfers for transfer with id: {} and branch with id: {}", id, branchId);
        List<Transfers> transfers = transfersRepository.findByIdOrBranchId(id, branchId);
        return transfersMapper.toTransfersDTO(transfers);   
    }

    public Page<TransfersDTO> filterByBranchId(String branchId, Pageable pageable) {
        log.info("Fetching transfers for branch with id: {} with pagination", branchId);
        Page<Transfers> transfers = transfersRepository.findByBranchId(branchId, pageable);
        return transfers.map(transfersMapper::toTransfersDTO);
    }
}

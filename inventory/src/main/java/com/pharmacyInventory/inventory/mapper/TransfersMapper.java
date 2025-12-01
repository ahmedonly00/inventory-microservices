package com.inventory.pharmacyInventory.mapper;

import com.inventory.pharmacyInventory.dtos.transfers.TransfersDTO;
import com.inventory.pharmacyInventory.model.Transfers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransfersMapper {

    public List<TransfersDTO> toTransfersDTO(List<Transfers> transfers) {
        if (transfers == null) {
            return null;
        }
        return transfers.stream()
                .map(this::toTransfersDTO)
                .collect(Collectors.toList());
    }

    public TransfersDTO toTransfersDTO(Transfers transfer) {
        if(transfer == null){
            return null;
        }

        return TransfersDTO.builder()
                .id(transfer.getId())
                .fromBranchId(transfer.getFromBranchId())
                .toBranchId(transfer.getToBranchId())
                .quantity(transfer.getQuantity())
                .transferDate(transfer.getTransferDate())
                .receivedDate(transfer.getReceivedDate())
                .status(transfer.getStatus())
                .fromLocation(transfer.getFromLocation())
                .toLocation(transfer.getToLocation())
                .notes(transfer.getNotes())
                .medicationId(transfer.getMedication() != null ? transfer.getMedication().getMedicationId() : null)
                .medicationName(transfer.getMedication() != null ? transfer.getMedication().getName() : null)
                .requestedById(transfer.getRequestedBy() != null ? transfer.getRequestedBy().getId() : null)
                .requestedByName(transfer.getRequestedBy() != null ? transfer.getRequestedBy().getName() : null)
                .build();
    }

    public Transfers toTransfers(TransfersDTO transfersDTO) {
        if(transfersDTO == null) {
            return null;
        }

        Transfers transfer = new Transfers();
        transfer.setId(transfersDTO.getId());
        transfer.setFromBranchId(transfersDTO.getFromBranchId());
        transfer.setToBranchId(transfersDTO.getToBranchId());
        transfer.setQuantity(transfersDTO.getQuantity());
        transfer.setTransferDate(transfersDTO.getTransferDate());
        transfer.setReceivedDate(transfersDTO.getReceivedDate());
        transfer.setStatus(transfersDTO.getStatus());
        transfer.setFromLocation(transfersDTO.getFromLocation());
        transfer.setToLocation(transfersDTO.getToLocation());
        transfer.setNotes(transfersDTO.getNotes());
                
        return transfer;
    } 
    
}

package com.pharmacyInventory.inventory.mapper;

import com.pharmacyInventory.inventory.dtos.transfers.TransfersDTO;
import com.pharmacyInventory.inventory.model.Transfers;
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
                .branchId(transfer.getBranchId())
                .toBranchId(transfer.getToBranchId())
                .quantity(transfer.getQuantity())
                .status(transfer.getStatus())
                .notes(transfer.getNotes())
                .medicationId(transfer.getMedication() != null ? transfer.getMedication().getMedicationId() : null)
                .medicationName(transfer.getMedication() != null ? transfer.getMedication().getName() : null)
                .build();
    }

    public Transfers toTransfers(TransfersDTO transfersDTO) {
        if(transfersDTO == null) {
            return null;
        }

        Transfers transfer = new Transfers();
        transfer.setId(transfersDTO.getId());
        transfer.setBranchId(transfersDTO.getBranchId());
        transfer.setToBranchId(transfersDTO.getToBranchId());
        transfer.setQuantity(transfersDTO.getQuantity());
        transfer.setStatus(transfersDTO.getStatus());
        transfer.setNotes(transfersDTO.getNotes());
                
        return transfer;
    } 
    
}

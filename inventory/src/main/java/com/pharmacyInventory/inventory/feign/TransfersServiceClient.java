package com.pharmacyInventory.inventory.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.pharmacyInventory.inventory.dtos.transfers.TransfersDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "transfers-service", url = "${transfers.service.url}")
public interface TransfersServiceClient {

    @PostMapping("/api/transfers")
    public TransfersDTO createTransfer(@RequestBody TransfersDTO transfersDTO);

    @GetMapping("/api/transfers")
    public List<TransfersDTO> getAllTransfers();

    @GetMapping("/api/transfers/{id}")
    public TransfersDTO getTransferById(@PathVariable Long id);

    @PutMapping("/api/transfers/{id}")
    public TransfersDTO updateTransfer(@PathVariable Long id, @RequestBody TransfersDTO transfersDTO);

    @GetMapping("/api/transfers/branch/{branchId}")
    public List<TransfersDTO> getTransfersByBranch(@PathVariable Long branchId);

    @GetMapping("/api/transfers/status/{status}")
    public List<TransfersDTO> updateTransfersByStatus(@PathVariable String status);

    @DeleteMapping("/api/transfers/{id}")
    public void deleteTransfer(@PathVariable Long id);

    
}

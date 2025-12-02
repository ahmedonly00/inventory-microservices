package com.pharmacyInventory.inventory.feign;

import java.util.ArrayList;
import java.util.List;

import com.pharmacyInventory.inventory.dtos.ReferenceSource.ReferenceSourceDTO;

public class ReferenceSourceServiceFallback implements ReferenceSourceServiceClient {

    @Override
    public List<ReferenceSourceDTO> getAllReferenceSources() {
        return new ArrayList<>();
    }

    @Override
    public ReferenceSourceDTO getReferenceSourceById(Long id) {
        throw new RuntimeException("Reference Source service is down");
    }

    @Override
    public ReferenceSourceDTO createReferenceSource(ReferenceSourceDTO referenceSourceDTO) {
        throw new RuntimeException("Reference Source service is down");
    }

    @Override
    public ReferenceSourceDTO updateReferenceSource(Long id, ReferenceSourceDTO referenceSourceDTO) {
        throw new RuntimeException("Reference Source service is down");
    }

    @Override
    public void deleteReferenceSource(Long id) {
        throw new RuntimeException("Reference Source service is down");
    }
    
}

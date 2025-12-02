package com.pharmacyInventory.inventory.feign;

import java.util.ArrayList;
import java.util.List;

import com.pharmacyInventory.inventory.dtos.suppliers.SuppliersDTO;
import com.pharmacyInventory.inventory.exception.ResourceNotFoundException;

public class SupplierServiceFallback implements SupplierServiceClient{
    
    @Override
    public List<SuppliersDTO> getAllSuppliers() {
        return new ArrayList<>();
    }

    @Override
    public SuppliersDTO getSupplierById(Long id) {
        throw new ResourceNotFoundException("Supplier service is down");
    }

    @Override
    public SuppliersDTO createSupplier(SuppliersDTO supplierDTO) {
        throw new ResourceNotFoundException("Supplier service is down");
    }

    @Override
    public SuppliersDTO updateSupplier(Long id, SuppliersDTO supplierDTO) {
        throw new ResourceNotFoundException("Supplier service is down");
    }

    @Override
    public void deleteSupplier(Long id) {   
        throw new ResourceNotFoundException("Supplier service is down");
    }

    @Override
    public void deactivateSupplier(Long id) {   
        throw new ResourceNotFoundException("Supplier service is down");
    }
}

package com.pharmacyInventory.inventory.feign;

public class SupplierServiceFallback implements SupplierServiceClient{
    
    @Override
    public List<SuppliersDTO> getAllSuppliers() {
        return new ArrayList<>();
    }

    @Override
    public SuppliersDTO getSupplierById(Long id) {
        return new RuntimeException("Supplier service is down");
    }

    @Override
    public SuppliersDTO createSupplier(SuppliersDTO supplierDTO) {
        return new RuntimeException("Supplier service is down");
    }

    @Override
    public SuppliersDTO updateSupplier(Long id, SuppliersDTO supplierDTO) {
        return new RuntimeException("Supplier service is down");
    }

    @Override
    public void deleteSupplier(Long id) {   
        throw new RuntimeException("Supplier service is down");
    }

    @Override
    public void deactivateSupplier(Long id) {   
        throw new RuntimeException("Supplier service is down");
    }
}

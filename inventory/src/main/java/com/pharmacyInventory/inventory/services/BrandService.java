package com.pharmacyInventory.inventory.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pharmacyInventory.inventory.dtos.Brands.BrandsDTO;
import com.pharmacyInventory.inventory.mapper.BrandMapper;
import com.pharmacyInventory.inventory.model.Brands;
import com.pharmacyInventory.inventory.repository.BrandRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
    
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public List<BrandsDTO> getAllBrands() {
        log.info("Fetching all brands");
        return brandMapper.toBrandsDTO(brandRepository.findAll());
    }
    
    public Optional<BrandsDTO> getBrandById(Long id) {
        log.info("Fetching brand with id: {}", id);
        return brandRepository.findById(id)
                .map(brandMapper::toBrandsDTO);
    }
    
    public BrandsDTO createBrand(BrandsDTO brandDTO) {
        log.info("Creating brand: {}", brandDTO);
        Brands brand = brandMapper.toBrands(brandDTO);
        return brandMapper.toBrandsDTO(brandRepository.save(brand));
    }
    
    public BrandsDTO updateBrand(Long id, BrandsDTO brandDTO) {
        log.info("Updating brand with id: {}", id);
        Brands existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        existingBrand.setName(brandDTO.getName());
        return brandMapper.toBrandsDTO(brandRepository.save(existingBrand));
    }
    
    public void deleteBrand(Long id) {
        log.info("Deleting brand with id: {}", id);
        brandRepository.deleteById(id);
    }  
    
}

package com.pharmacyInventory.inventory.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pharmacyInventory.inventory.dtos.Brands.BrandsDTO;
import com.pharmacyInventory.inventory.model.Brands;

@Component
public class BrandMapper {

    public List<BrandsDTO> toBrandsDTO(List<Brands> brands) {
        if (brands == null) {
            return null;
        }
        return brands.stream()
                .map(this::toBrandsDTO)
                .collect(Collectors.toList());
    }
    
    public BrandsDTO toBrandsDTO(Brands brands) {
        if (brands == null) {
            return null;
        }
        return BrandsDTO.builder()
                .id(brands.getId())
                .name(brands.getName())
                .build();
    }
    
    public Brands toBrands(BrandsDTO brandsDTO) {
        if (brandsDTO == null) {
            return null;
        }
        return Brands.builder()
                .id(brandsDTO.getId())
                .name(brandsDTO.getName())
                .build();
    }
}

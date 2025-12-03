package com.pharmacyInventory.inventory.dtos.categories;

import com.pharmacyInventory.inventory.Enum.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriesDTO {
    private Long id;
    private String branchId;
    private String name;
    private CategoryType type;
    private String color;
    private String description;
    private Boolean isActive;
    private List<Long> medicationIds;
}

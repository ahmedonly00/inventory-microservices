package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.medications.MedicationsDTO;
import com.pharmacyInventory.inventory.mapper.MedicationsMapper;
import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.model.Categories;
import com.pharmacyInventory.inventory.repository.MedicationsRepository;
import com.pharmacyInventory.inventory.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationsService {

    private final MedicationsRepository medicationsRepository;
    private final MedicationsMapper medicationsMapper;
    private final CategoriesRepository categoriesRepository;

    public List<MedicationsDTO> getAllMedications() {
        log.info("Fetching all medications");
        List<Medications> medications = medicationsRepository.findAll();
        return medicationsMapper.toMedicationsDTO(medications);
    }

    public MedicationsDTO getMedicationById(Long id) {
        log.info("Fetching medication with id: {}", id);
        Medications medication = medicationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
        return medicationsMapper.toMedicationsDTO(medication);
    }

    public MedicationsDTO createMedication(MedicationsDTO medicationDTO) {
        log.info("Creating new medication: {}", medicationDTO.getName());
        
        // Check if medication with same name already exists
        if (medicationsRepository.existsByName(medicationDTO.getName())) {
            throw new RuntimeException("Medication with name '" + medicationDTO.getName() + "' already exists");
        }

        Medications medication = medicationsMapper.toMedications(medicationDTO);
        medication.setCreatedAt(LocalDateTime.now());
        medication.setUpdatedAt(LocalDateTime.now());
        medication.setIsActive(true);

        Medications saved = medicationsRepository.save(medication);
        log.info("Created medication with id: {}", saved.getId());
        return medicationsMapper.toMedicationsDTO(saved);
    }

    public MedicationsDTO updateMedication(Long id, MedicationsDTO medicationDTO) {
        log.info("Updating medication with id: {}", id);
        
        Medications existing = medicationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));

        // Update fields
        existing.setName(medicationDTO.getName());
        
        // Update form relationship if formId is provided
        if (medicationDTO.getFormId() != null) {
            Categories form = categoriesRepository.findById(medicationDTO.getFormId())
                .orElseThrow(() -> new RuntimeException("Form not found with id: " + medicationDTO.getFormId()));
            existing.setForm(form);
        }
        
        existing.setStrength(medicationDTO.getStrength());
        existing.setStockQuantity(medicationDTO.getStockQuantity());
        existing.setReorderLevel(medicationDTO.getReorderLevel());
        existing.setPrice(medicationDTO.getPrice());
        existing.setBatchNumber(medicationDTO.getBatchNumber());
        existing.setExpiryDate(medicationDTO.getExpiryDate());
        existing.setUpdatedAt(LocalDateTime.now());

        Medications updated = medicationsRepository.save(existing);
        log.info("Updated medication with id: {}", id);
        return medicationsMapper.toMedicationsDTO(updated);
    }

    public void deleteMedication(Long id) {
        log.info("Deleting medication with id: {}", id);
        
        Medications medication = medicationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));

        medicationsRepository.delete(medication);
        log.info("Deleted medication with id: {}", id);
    }

    public void addStock(Long id, Integer quantity) {
        log.info("Adding {} units of stock to medication id: {}", quantity, id);
        
        Medications medication = medicationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));

        medication.setStockQuantity(medication.getStockQuantity() + quantity);
        medication.setUpdatedAt(LocalDateTime.now());

        medicationsRepository.save(medication);
        log.info("Added {} units of stock to medication id: {}", quantity, id);
    }

    public List<MedicationsDTO> searchMedications(String query) {
        log.info("Searching medications with query: {}", query);
        
        List<Medications> medications = medicationsRepository.findByNameContainingIgnoreCase(query);
        return medicationsMapper.toMedicationsDTO(medications);
    }

    public List<MedicationsDTO> uploadInventory(MultipartFile file) throws IOException {
        log.info("Uploading inventory from file: {}", file.getOriginalFilename());
        
        List<MedicationsDTO> medications = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
            Row row = sheet.getRow(i);
            if (row != null) {
                try {
                    MedicationsDTO medication = MedicationsDTO.builder()
                            .name(getCellValue(row.getCell(0)))
                            .form(getCellValue(row.getCell(1)))
                            .strength(getCellValue(row.getCell(2)))
                            .stockQuantity(getIntValue(row.getCell(3)))
                            .reorderLevel(getIntValue(row.getCell(4)))
                            .price(getFloatValue(row.getCell(5)))
                            .batchNumber(getCellValue(row.getCell(6)))
                            .expiryDate(getLocalDateValue(row.getCell(7)))
                            .isActive(true)
                            .build();

                    medications.add(createMedication(medication));
                } catch (Exception e) {
                    log.warn("Error processing row {}: {}", i + 1, e.getMessage());
                }
            }
        }

        workbook.close();
        log.info("Uploaded {} medications from file", medications.size());
        return medications;
    }

    public byte[] downloadTemplate() throws IOException {
        log.info("Generating inventory template");
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Medications");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "Form", "Strength", "Stock Quantity", "Reorder Level", "Price", "Batch Number", "Expiry Date"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        log.info("Generated inventory template");
        return outputStream.toByteArray();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Integer getIntValue(Cell cell) {
        if (cell == null) return 0;
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private Float getFloatValue(Cell cell) {
        if (cell == null) return 0f;
        try {
            return (float) cell.getNumericCellValue();
        } catch (Exception e) {
            return 0f;
        }
    }

    private LocalDate getLocalDateValue(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
        } catch (Exception e) {
            log.warn("Error parsing date from cell");
        }
        return null;
    }
}

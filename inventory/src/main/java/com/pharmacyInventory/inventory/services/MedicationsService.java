package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.medications.MedicationsDTO;
import com.pharmacyInventory.inventory.mapper.MedicationsMapper;
import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.model.Categories;
import com.pharmacyInventory.inventory.Enum.StockStatus;
import com.pharmacyInventory.inventory.repository.MedicationsRepository;

import jakarta.transaction.Transactional;

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

    public List<MedicationsDTO> getAllMedications( String branchId) {
        log.info("Fetching all medications");
        List<Medications> medications = medicationsRepository.findAllByBranchId(branchId);
        return medicationsMapper.toMedicationsDTO(medications);
    }

    public MedicationsDTO getMedicationById(Long medicationId, String branchId) {
        log.info("Fetching medication with id: {}", medicationId);
        Medications medication = medicationsRepository.findByMedicationIdAndBranchId(medicationId, branchId)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));
        return medicationsMapper.toMedicationsDTO(medication);
    }

    public MedicationsDTO createMedication(MedicationsDTO medicationDTO, String branchId) {
        log.info("Creating new medication: {}", medicationDTO.getName());
        
        // Check if medication with same name already exists
        if (medicationsRepository.existsByName(medicationDTO.getName())) {
            throw new RuntimeException("Medication with name '" + medicationDTO.getName() + "' already exists");
        }

        if(branchId == null || branchId.isEmpty()) {
            throw new RuntimeException("Branch ID is required");
        }

        Medications medication = medicationsMapper.toMedications(medicationDTO);
        medication.setBranchId(branchId);   
        medication.setCreatedAt(LocalDateTime.now());
        medication.setUpdatedAt(LocalDateTime.now());
        medication.setStockStatus(StockStatus.IN_STOCK);

        Medications saved = medicationsRepository.save(medication);
        log.info("Created medication with id: {}", saved.getMedicationId());
        return medicationsMapper.toMedicationsDTO(saved);
    }

    public MedicationsDTO updateMedication(Long medicationId, MedicationsDTO medicationDTO, String branchId) {
        log.info("Updating medication with id: {}", medicationId);
        
        Medications existing = medicationsRepository.findByMedicationIdAndBranchId(medicationId, branchId)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));

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
        log.info("Updated medication with id: {}", medicationId);
        return medicationsMapper.toMedicationsDTO(updated);
    }

    public void deleteMedication(Long medicationId, String branchId) {
        log.info("Deleting medication with id: {}", medicationId);
        
        Medications medication = medicationsRepository.findByMedicationIdAndBranchId(medicationId, branchId)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));

        medicationsRepository.delete(medication);
        log.info("Deleted medication with id: {}", medicationId);
    }

    public void addStock(Long medicationId, Integer quantity, String branchId) {
        log.info("Adding {} units of stock to medication id: {}", quantity, medicationId);
        
        Medications medication = medicationsRepository.findByMedicationIdAndBranchId(medicationId, branchId)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));

        medication.setStockQuantity(medication.getStockQuantity() + quantity);
        medication.setUpdatedAt(LocalDateTime.now());

        medicationsRepository.save(medication);
        log.info("Added {} units of stock to medication id: {}", quantity, medicationId);
    }

    public List<MedicationsDTO> searchMedications(String query, String branchId) {
        log.info("Searching medications with query: {}", query);
        
        List<Medications> medications = medicationsRepository.findByNameContainingIgnoreCaseAndBranchId(query, branchId);
        return medicationsMapper.toMedicationsDTO(medications);
    }

    public List<MedicationsDTO> filterMedicationsByCategory(Long categoryId, String branchId) {
        log.info("Filtering medications by category id: {}", categoryId);
        
        List<Medications> medications = medicationsRepository.findByCategoryIdAndBranchId(categoryId, branchId);
        return medicationsMapper.toMedicationsDTO(medications);
    }

    public List<MedicationsDTO> filterMedicationsByStatus(StockStatus stockStatus, String branchId) {
        log.info("Filtering medications by status: {}", stockStatus);
        
        List<Medications> medications = medicationsRepository.findByStockStatusAndBranchId(stockStatus, branchId);
        return medicationsMapper.toMedicationsDTO(medications);
    }

    @Transactional
    public void updateStockStatus(Long medicationId, String branchId) {
        Medications medication = medicationsRepository.findByMedicationIdAndBranchId(medicationId, branchId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        
        if (medication.getStockQuantity() <= 0) {
            medication.setStockStatus(StockStatus.OUT_OF_STOCK);
        } else if (medication.getStockQuantity() <= medication.getReorderLevel()) {
            medication.setStockStatus(StockStatus.LOW_STOCK);
        } else {
            medication.setStockStatus(StockStatus.IN_STOCK);
        }
        
        medicationsRepository.save(medication);
    }

    @Transactional
    public MedicationsDTO updateStockQuantity(Long medicationId, Integer quantity, String branchId) {
        Medications medication = medicationsRepository.findByMedicationIdAndBranchId(medicationId, branchId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        
        medication.setStockQuantity(quantity);
        medicationsRepository.save(medication);
        
        // Update the stock status
        updateStockStatus(medicationId, branchId);
        
        return medicationsMapper.toMedicationsDTO(medication);
    }

    public List<MedicationsDTO> uploadInventory(MultipartFile file, String branchId) throws IOException {
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
                            .build();

                    medication.setBranchId(branchId);
                    medication.setCreatedAt(LocalDateTime.now());
                    medication.setUpdatedAt(LocalDateTime.now());
                    medication.setStockStatus(StockStatus.IN_STOCK);
                    medications.add(createMedication(medication, branchId));
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
        String[] headers = {"Medication Name", "Form", "Strength","Category","Supplier", "Stock Quantity", "Reorder Level", "Price", "Batch Number", "Expiry Date","Stock Status"};
        
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

    public byte[] exportToExcel(String branchId) throws IOException {
        log.info("Exporting inventory to Excel for branch: {}", branchId);
        
        // Get medications data
        List<MedicationsDTO> medications = getAllMedications(branchId);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Medications");
            
            // Create header row
            String[] headers = {
                "ID", "Name", "Form", "Strength", "Category", "Stock Quantity", "Reorder Level","Price","Supplier" ,
                "Batch Number", "Expiry Date", "Status"
            };
            
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Fill data rows
            int rowNum = 1;
            for (MedicationsDTO med : medications) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(med.getMedicationId() != null ? med.getMedicationId() : 0);
                row.createCell(1).setCellValue(med.getName() != null ? med.getName() : "");
                row.createCell(2).setCellValue(med.getFormName() != null ? med.getFormName() : "");
                row.createCell(3).setCellValue(med.getStrength() != null ? med.getStrength() : "");
                row.createCell(4).setCellValue(med.getCategoryName() != null ? med.getCategoryName() : "");
                row.createCell(5).setCellValue(med.getStockQuantity() != null ? med.getStockQuantity() : 0);
                row.createCell(6).setCellValue(med.getReorderLevel() != null ? med.getReorderLevel() : 0);
                row.createCell(7).setCellValue(med.getPrice() != null ? med.getPrice() : 0.0);
                row.createCell(8).setCellValue(med.getSupplierName() != null ? med.getSupplierName() : "");
                row.createCell(9).setCellValue(med.getBatchNumber() != null ? med.getBatchNumber() : "");
                
                // Format date  
                if (med.getExpiryDate() != null) {
                    Cell dateCell = row.createCell(6);
                    dateCell.setCellValue(med.getExpiryDate());
                    CellStyle dateCellStyle = workbook.createCellStyle();
                    dateCellStyle.setDataFormat(workbook.getCreationHelper()
                        .createDataFormat()
                        .getFormat("dd/MM/yyyy"));
                    dateCell.setCellStyle(dateCellStyle);
                } else {
                    row.createCell(6).setCellValue("");
                }
                
                row.createCell(7).setCellValue(med.getDescription() != null ? med.getDescription() : "");
                row.createCell(8).setCellValue(med.getStockStatus() != null ? med.getStockStatus().name() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
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

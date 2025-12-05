package com.pharmacyInventory.inventory.services;

import com.pharmacyInventory.inventory.dtos.equivalents.EquivalentsDTO;
import com.pharmacyInventory.inventory.exception.ResourceNotFoundException;
import com.pharmacyInventory.inventory.mapper.EquivalentsMapper;
import com.pharmacyInventory.inventory.model.Brands;
import com.pharmacyInventory.inventory.model.Equivalents;
import com.pharmacyInventory.inventory.model.Medications;
import com.pharmacyInventory.inventory.repository.BrandRepository;
import com.pharmacyInventory.inventory.repository.EquivalentsRepository;
import com.pharmacyInventory.inventory.repository.MedicationsRepository;
import com.pharmacyInventory.inventory.repository.ReferenceSourceRepository;

import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;




@Service
@RequiredArgsConstructor
@Slf4j
public class EquivalentsService {

    private final EquivalentsRepository equivalentsRepository;
    private final MedicationsRepository medicationsRepository;
    private final EquivalentsMapper equivalentsMapper;
    private final ReferenceSourceRepository referenceSourceRepository;
    private final BrandRepository brandRepository;
    


    public List<EquivalentsDTO> getAllEquivalentsByBranch(String branchId) {
        log.info("Fetching all equivalents by branch: {}", branchId);
        List<Equivalents> equivalents = equivalentsRepository.findByBranchId(branchId);
        return equivalentsMapper.toEquivalentsDTO(equivalents);
    }

    public EquivalentsDTO getEquivalentById(Long id, String branchId) {
        return equivalentsMapper.toEquivalentsDTO(
            equivalentsRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new RuntimeException("Equivalent not found with id: " + id + " and branch: " + branchId))
        );
    }

    public List<EquivalentsDTO> getEquivalentsByMedicationId(Long medicationId) {
        log.info("Fetching equivalents for medication with id: {}", medicationId);
        
        Medications medication = medicationsRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medicationId));
        
        List<Equivalents> equivalents = equivalentsRepository.findByOriginalMedication(medication);
        return equivalentsMapper.toEquivalentsDTO(equivalents);
    }

    public EquivalentsDTO createEquivalent(EquivalentsDTO equivalentDTO, String branchId) {
        log.info("Creating new equivalent: {} - {} for branch: {}", equivalentDTO.getInn(), equivalentDTO.getStrength(), branchId);
        
        // Validate medications exist
        Medications originalMedication = medicationsRepository.findById(equivalentDTO.getOriginalMedicationId())
                .orElseThrow(() -> new RuntimeException("Original medication not found with id: " + equivalentDTO.getOriginalMedicationId()));
        
        Medications equivalentMedication = medicationsRepository.findById(equivalentDTO.getEquivalentMedicationId())
                .orElseThrow(() -> new RuntimeException("Equivalent medication not found with id: " + equivalentDTO.getEquivalentMedicationId()));

        // Validate all brands exist
        List<Brands> brands = brandRepository.findAllById(equivalentDTO.getBrandIds());
        if (brands.size() != equivalentDTO.getBrandIds().size()) {
            throw new ResourceNotFoundException("One or more brands not found");
        }
                
        Equivalents equivalent = equivalentsMapper.toEquivalents(equivalentDTO);
        equivalent.setOriginalMedication(originalMedication);
        equivalent.setEquivalentMedication(equivalentMedication);
        equivalent.setCreatedAt(LocalDateTime.now());
        equivalent.setBranchId(branchId);

        Equivalents saved = equivalentsRepository.save(equivalent);
        log.info("Created equivalent with id: {} for branch: {}", saved.getId(), branchId);
        return equivalentsMapper.toEquivalentsDTO(saved);
    }

    public EquivalentsDTO updateEquivalent(Long id, EquivalentsDTO equivalentDTO, String branchId) {
        log.info("Updating equivalent with id: {} for branch: {}", id, branchId);
        
        Equivalents existing = equivalentsRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new RuntimeException("Equivalent not found with id: " + id + " for branch: " + branchId));

        // Update fields
        existing.setInn(equivalentDTO.getInn());
        existing.setForm(equivalentDTO.getForm());
        existing.setStrength(equivalentDTO.getStrength());
        existing.setBrandNames(equivalentDTO.getBrandNames());
        existing.setReferenceSourceName(equivalentDTO.getReferenceSourceName());

        // Update medications if changed
        if (!equivalentDTO.getOriginalMedicationId().equals(existing.getOriginalMedication().getMedicationId())) {
            Medications originalMedication = medicationsRepository.findById(equivalentDTO.getOriginalMedicationId())
                    .orElseThrow(() -> new RuntimeException("Original medication not found with id: " + equivalentDTO.getOriginalMedicationId()));
            existing.setOriginalMedication(originalMedication);
        }

        if (!equivalentDTO.getEquivalentMedicationId().equals(existing.getEquivalentMedication().getMedicationId())) {
            Medications equivalentMedication = medicationsRepository.findById(equivalentDTO.getEquivalentMedicationId())
                    .orElseThrow(() -> new RuntimeException("Equivalent medication not found with id: " + equivalentDTO.getEquivalentMedicationId()));
            existing.setEquivalentMedication(equivalentMedication);
        }


        existing.setBranchId(branchId);
        Equivalents updated = equivalentsRepository.save(existing);
        log.info("Updated equivalent with id: {} for branch: {}", id, branchId);
        return equivalentsMapper.toEquivalentsDTO(updated);
    }

    public void deleteEquivalent(Long id, String branchId) {
        log.info("Deleting equivalent with id: {} for branch: {}", id, branchId);
        
        if (!equivalentsRepository.existsByIdAndBranchId(id, branchId)) {
            throw new RuntimeException("Equivalent not found with id: " + id + " for branch: " + branchId);
        }

        equivalentsRepository.deleteByIdAndBranchId(id, branchId);
        log.info("Deleted equivalent with id: {} for branch: {}", id, branchId);
    }

    public List<EquivalentsDTO> searchEquivalents(String query, String branchId) {
        log.info("Searching equivalents for query: {} for branch: {}", query, branchId);
        List<Equivalents> equivalents = equivalentsRepository.findByInnContainingOrBrandNameContaining(query, branchId);
        return equivalentsMapper.toEquivalentsDTO(equivalents);
    }

    public List<EquivalentsDTO> filterEquivalentsByFormsAndReferenceSource(List<String> forms, List<String> referenceSources, String branchId) {
        log.info("Filtering equivalents by forms: {} and reference sources: {} for branch: {}", forms, referenceSources, branchId);
        List<Equivalents> equivalents = equivalentsRepository.findByFormInAndReferenceSourceNameInAndBranchId(forms, referenceSources, branchId);
        return equivalentsMapper.toEquivalentsDTO(equivalents);
    }

    //Import Equivalents from Excel and CSV
    public List<EquivalentsDTO> importEquivalentsFromFile(MultipartFile file, String branchId) throws Exception {
        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Invalid file format. Only XLSX files are allowed.");
        }

        if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            return importFromExcel(file.getInputStream(), branchId);
        } else if (fileName.endsWith(".csv")) {
            return importFromCsv(file.getInputStream(), branchId);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Please upload an Excel (.xlsx, .xls) or CSV file.");
        }    
        
    }

    private List<EquivalentsDTO> importFromExcel(InputStream inputStream, String branchId) throws IOException {
        List<EquivalentsDTO> importedEquivalents = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                try {
                    EquivalentsDTO dto = new EquivalentsDTO();
                    
                    // Map Excel columns to DTO fields
                    dto.setInn(getCellValue(currentRow.getCell(0))); // INN
                    dto.setForm(getCellValue(currentRow.getCell(1))); // Form
                    dto.setStrength(getCellValue(currentRow.getCell(2))); // Strength
                    dto.setReferenceSourceName(getCellValue(currentRow.getCell(3))); // Reference Source
                    
                    // For brand names (comma-separated in Excel)
                    String brandNamesStr = getCellValue(currentRow.getCell(4));
                    if (brandNamesStr != null && !brandNamesStr.isEmpty()) {
                        dto.setBrandNames(List.of(brandNamesStr.split("\\s*,\\s*")));
                    }
                    
                    // For brand IDs (comma-separated in Excel)
                    String brandIdsStr = getCellValue(currentRow.getCell(5));
                    if (brandIdsStr != null && !brandIdsStr.isEmpty()) {
                        dto.setBrandIds(Arrays.stream(brandIdsStr.split("\\s*,\\s*"))
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList()));
                    }
                    
                    // Save the equivalent
                    log.info("Saving equivalent: INN={}, Form={}, Strength={}", dto.getInn(), dto.getForm(), dto.getStrength());
                    dto.setBranchId(branchId);
                    EquivalentsDTO savedDto = createEquivalent(dto, branchId);
                    log.info("Successfully saved equivalent with ID: {}", savedDto.getId());
                    importedEquivalents.add(savedDto);
                    
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", currentRow.getRowNum() + 1, e.getMessage());
                    // Continue with next row even if one fails
                }
            }
        } catch (Exception e) {
            log.error("Error importing from Excel", e);
            throw new RuntimeException("Failed to import equivalents from Excel", e);
        }
        return importedEquivalents;
    }

    private List<EquivalentsDTO> importFromCsv(InputStream inputStream, String branchId) throws IOException {
        List<EquivalentsDTO> importedEquivalents = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)))) {
            String[] line;
            // Skip header
            reader.readNext();
            int lineNumber = 1; // Start from 1 to account for header row
            
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {
                    if (line.length < 6) {
                        log.warn("Skipping invalid CSV line {}: {}", lineNumber, String.join(",", line));
                        continue;
                    }
                    
                    EquivalentsDTO dto = new EquivalentsDTO();
                    dto.setInn(line[0].trim()); // INN
                    dto.setForm(line[1].trim()); // Form
                    dto.setStrength(line[2].trim()); // Strength
                    dto.setReferenceSourceName(line[3].trim()); // Reference Source
                    
                    // Brand names (comma-separated in CSV)
                    if (!line[4].trim().isEmpty()) {
                        dto.setBrandNames(Arrays.stream(line[4].split("\\s*,\\s*"))
                                            .map(String::trim)
                                            .collect(Collectors.toList()));
                    }
                    
                    // Brand IDs (comma-separated in CSV)
                    if (line.length > 5 && !line[5].trim().isEmpty()) {
                        List<Long> brandIds = Arrays.stream(line[5].split("\\s*,\\s*"))
                                                .map(String::trim)
                                                .map(Long::parseLong)
                                                .collect(Collectors.toList());
                        
                        // Validate brand IDs exist
                        List<Long> existingBrandIds = validateBrandIds(brandIds);
                        if (existingBrandIds.size() != brandIds.size()) {
                            log.warn("Skipping line {}: One or more brand IDs not found", lineNumber);
                            continue;
                        }
                        
                        dto.setBrandIds(existingBrandIds);
                    } else {
                        log.warn("Skipping line {}: Brand IDs are required", lineNumber);
                        continue;
                    }
                    
                    // Save the equivalent
                    dto.setBranchId(branchId);
                    EquivalentsDTO savedDto = createEquivalent(dto, branchId);
                    importedEquivalents.add(savedDto);
                    log.info("Successfully imported equivalent for INN: {}, Form: {}, Strength: {}", 
                           dto.getInn(), dto.getForm(), dto.getStrength());
                    
                    log.info("Imported equivalent with ID: {}", savedDto.getId());
                    
                } catch (Exception e) {
                    log.error("Error processing CSV line {}: {}", lineNumber, String.join(",", line), e);
                    // Continue with next line even if one fails
                }
            }
        
        } catch (Exception e) {
            log.error("Error importing from CSV", e);
            throw new RuntimeException("Failed to import equivalents from CSV", e);
        }
        
        log.info("Successfully imported {} equivalents", importedEquivalents.size());
        return importedEquivalents;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            case FORMULA:
                return cell.getCellFormula();
            case ERROR:
                return "ERROR";
            default:
                return "";
        }
    }

    // Validates that all brand IDs exist in the database
    private List<Long> validateBrandIds(List<Long> brandIds) {
        if (brandIds == null || brandIds.isEmpty()) {
            return Collections.emptyList();
        }
    
    // Get all existing brand IDs in a single query
        List<Long> existingBrands = brandRepository.findAllById(brandIds).stream()
            .map(Brands::getId)
            .collect(Collectors.toList());
    
    // Check if all requested IDs exist
        List<Long> missingBrands = brandIds.stream()
            .filter(id -> !existingBrands.contains(id))
            .collect(Collectors.toList());
    
        if (!missingBrands.isEmpty()) {
            log.warn("The following brand IDs were not found: {}", missingBrands);
        }
    
        return existingBrands;
    }

    // Export equivalents to file (CSV or Excel)
    public byte[] exportEquivalentsToFile(String format, String branchId) throws IOException {
        // Get all equivalents for the specified branch
        List<Equivalents> equivalents = equivalentsRepository.findByBranchId(branchId);
        
        log.info("Exporting {} equivalents for branch: {}", equivalents.size(), branchId);
        
        if ("csv".equalsIgnoreCase(format)) {
            return exportToCsv(equivalents);
        } else {
            return exportToExcel(equivalents);
        }
    }

    private byte[] exportToExcel(List<Equivalents> equivalents) throws IOException {
        log.info("Starting Excel export for {} equivalents", equivalents.size());
        try (Workbook workbook = new XSSFWorkbook(); 
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Equivalents");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "INN", "Form", "Strength", "Reference Source", 
                "Brand Names", "Created At"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Fill data
            int rowNum = 1;
            for (Equivalents eq : equivalents) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(eq.getInn());
                row.createCell(1).setCellValue(eq.getForm());
                row.createCell(2).setCellValue(eq.getStrength());
                row.createCell(3).setCellValue(eq.getReferenceSourceName());
                row.createCell(4).setCellValue(String.join(", ", eq.getBrandNames()));
                row.createCell(5).setCellValue(eq.getCreatedAt() != null ? 
                        eq.getCreatedAt().toString() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        
        } catch (IOException e) {
            throw new RuntimeException("Failed to export equivalents to Excel", e);
        }
    }

    private byte[] exportToCsv(List<Equivalents> equivalents) throws IOException {
        log.info("Starting CSV export for {} equivalents", equivalents.size());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            CSVPrinter csvPrinter = new CSVPrinter(writer, 
                CSVFormat.DEFAULT.builder()
                    .setHeader("INN", "Form", "Strength", "Reference Source", "Brand Names", "Created At")
                    .build())) {
            
            for (Equivalents eq : equivalents) {
                csvPrinter.printRecord(
                    eq.getInn(),
                    eq.getForm(),
                    eq.getStrength(),
                    eq.getReferenceSourceName(),
                    String.join(", ", eq.getBrandNames()),
                    eq.getCreatedAt() != null ? eq.getCreatedAt().toString() : ""
                );
            }
            
            csvPrinter.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export equivalents to CSV", e);
        }
    }
    
    // Get statistics about equivalent mappings for a specific branch
    public Map<String, Object> getEquivalentsStatsByBranch(String branchId) {
        log.info("Fetching equivalents statistics for branch: {}", branchId);
        
        Map<String, Object> stats = new LinkedHashMap<>();
        
        try {
            // Add branch ID to the response
            stats.put("branchId", branchId);
            
            // Total number of equivalent mappings for the branch
            long totalMappings = equivalentsRepository.countByBranchId(branchId);
            stats.put("totalMappings", totalMappings);
            
            // Total number of brands for the branch
            long totalBrands = brandRepository.countByBranchId(branchId);
            stats.put("totalBrands", totalBrands);
            
            // Total unique INNs for the branch
            long totalUniqueInns = equivalentsRepository.countDistinctInnByBranchId(branchId);
            stats.put("totalUniqueInns", totalUniqueInns);
            
            // Total data sources for the branch
            long totalDataSources = referenceSourceRepository.countByBranchId(branchId);
            stats.put("totalDataSources", totalDataSources);
            
            // Count by form for the branch
            List<Object[]> formsCount = equivalentsRepository.countByFormAndBranchId(branchId);
            Map<String, Long> mappingsByForm = formsCount.stream()
                    .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                    ));
            stats.put("mappingsByForm", mappingsByForm);
            
            // Count by data source for the branch
            List<Object[]> sourcesCount = equivalentsRepository.countByReferenceSourceAndBranchId(branchId);
            Map<String, Long> mappingsBySource = sourcesCount.stream()
                    .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                    ));
            stats.put("mappingsBySource", mappingsBySource);
            
            // Top 10 most common INNs in this branch
            List<Object[]> topInns = equivalentsRepository.findTopInnsByBranchId(branchId, PageRequest.of(0, 10));
            List<Map<String, Object>> topInnsList = topInns.stream()
                    .map(arr -> {
                        Map<String, Object> innInfo = new HashMap<>();
                        innInfo.put("inn", arr[0]);
                        innInfo.put("count", arr[1]);
                        return innInfo;
                    })
                    .collect(Collectors.toList());
            stats.put("topInns", topInnsList);
            
            log.info("Retrieved equivalents statistics for branch {}: {}", branchId, stats);
            return stats;
            
        } catch (Exception e) {
            log.error("Error fetching equivalents statistics for branch: " + branchId, e);
            throw new RuntimeException("Failed to fetch equivalents statistics for branch: " + branchId, e);
        }
    }

}

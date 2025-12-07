package com.pharmacyInventory.inventory.controller;

import com.pharmacyInventory.inventory.dtos.equivalents.EquivalentsDTO;
import com.pharmacyInventory.inventory.services.EquivalentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equivalents")
@CrossOrigin(origins = "*")
public class EquivalentsController {

    private final EquivalentsService equivalentsService;

    @GetMapping(value = "/getAllEquivalents/{branchId}")
    public ResponseEntity<List<EquivalentsDTO>> getAllEquivalentsByBranch(@PathVariable String branchId) {
        log.info("Fetching all equivalents for branch: {}", branchId);
        List<EquivalentsDTO> equivalents = equivalentsService.getAllEquivalentsByBranch(branchId);
        return ResponseEntity.ok(equivalents);
    }

    @GetMapping(value = "/medication/{medicationId}")
    public ResponseEntity<List<EquivalentsDTO>> getEquivalentsByMedicationId(@PathVariable Long medicationId) {
        log.info("Fetching equivalents for medication with id: {}", medicationId);
        List<EquivalentsDTO> equivalents = equivalentsService.getEquivalentsByMedicationId(medicationId);
        return ResponseEntity.ok(equivalents);
    }

    @GetMapping("/{id}/branch/{branchId}")
    public ResponseEntity<EquivalentsDTO> getEquivalentById(
            @PathVariable Long id, 
            @PathVariable String branchId) {
        return ResponseEntity.ok(equivalentsService.getEquivalentById(id, branchId));
    }

    @PostMapping(value = "/createEquivalent/{branchId}")
    public ResponseEntity<?> createEquivalent(@PathVariable String branchId, @Valid @RequestBody EquivalentsDTO equivalentDTO) {
        try {
            
            log.info("Creating equivalent for branch: {}", branchId);
            EquivalentsDTO created = equivalentsService.createEquivalent(equivalentDTO, branchId);
            log.info("Created new equivalent with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating equivalent", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/updateEquivalent/{id}/branch/{branchId}")
    public ResponseEntity<?> updateEquivalent(@PathVariable Long id, @PathVariable String branchId, @Valid @RequestBody EquivalentsDTO equivalentDTO) {
        try {
            log.info("Updating equivalent with id: {} for branch: {}", id, branchId);
            equivalentDTO.setBranchId(branchId);
            EquivalentsDTO updated = equivalentsService.updateEquivalent(id, equivalentDTO, branchId);
            log.info("Updated equivalent with id: {} for branch: {}", id, branchId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating equivalent with id: {} for branch: {}", id, branchId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteEquivalent/{id}/branch/{branchId}")
    public ResponseEntity<?> deleteEquivalent(@PathVariable Long id, @PathVariable String branchId) {
        try {
            log.info("Deleting equivalent with id: {} for branch: {}", id, branchId);
            equivalentsService.deleteEquivalent(id, branchId);
            log.info("Deleted equivalent with id: {} for branch: {}", id, branchId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting equivalent with id: {} for branch: {}", id, branchId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search/byBranch/{branchId}")
    public ResponseEntity<List<EquivalentsDTO>> searchEquivalents(@PathVariable String branchId, @RequestParam String query) {
        List<EquivalentsDTO> equivalents = equivalentsService.searchEquivalents(query, branchId);
        return ResponseEntity.ok(equivalents);
    }

    @GetMapping("/filter/byBranch/{branchId}")
    public ResponseEntity<List<EquivalentsDTO>> filterEquivalentsByFormsOrReferenceSource(
            @PathVariable String branchId,
            @RequestParam(required = false) List<String> forms,
            @RequestParam(required = false) List<String> referenceSources) {
        List<EquivalentsDTO> equivalents = equivalentsService.filterEquivalentsByFormsOrReferenceSource(
            forms, 
            referenceSources, 
            branchId
        );
        return ResponseEntity.ok(equivalents);
    }

    @PostMapping("/import/byBranch/{branchId}")
    public ResponseEntity<List<EquivalentsDTO>> importEquivalents(@RequestParam("file") MultipartFile file, @PathVariable String branchId) {
        try {
            List<EquivalentsDTO> imported = equivalentsService.importEquivalentsFromFile(file, branchId);
            return ResponseEntity.ok(imported);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/export/excel/byBranch/{branchId}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String branchId) throws IOException {
        byte[] data = equivalentsService.exportEquivalentsToFile("excel", branchId);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equivalents_export_" + branchId + ".xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(data.length)
            .body(data);
    }

    @GetMapping("/export/csv/byBranch/{branchId}")
    public ResponseEntity<byte[]> exportToCsv(@PathVariable String branchId) throws IOException {
        byte[] data = equivalentsService.exportEquivalentsToFile("csv", branchId);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equivalents_export_" + branchId + ".csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .contentLength(data.length)
            .body(data);
    }

    @GetMapping("/stats/branch/{branchId}")
    public ResponseEntity<Map<String, Object>> getEquivalentsStatsByBranch(
            @PathVariable String branchId) {
        log.info("Fetching equivalents statistics for branch: {}", branchId);
        try {
            Map<String, Object> stats = equivalentsService.getEquivalentsStatsByBranch(branchId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching equivalents statistics for branch: " + branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

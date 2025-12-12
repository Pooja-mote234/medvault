package com.medibook.medibook_backend.controller;

import com.medibook.medibook_backend.dto.MedicalRecordDTO;
import com.medibook.medibook_backend.dto.UploadMedicalRecordRequest;
import com.medibook.medibook_backend.entity.MedicalRecord;
import com.medibook.medibook_backend.service.MedicalRecordService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping(value = "/patient/{patientId}/medical-records", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicalRecordDTO> uploadMedicalRecord(
            @PathVariable Long patientId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") UploadMedicalRecordRequest request) { // Accepting JSON as part

        MedicalRecordDTO savedRecord = medicalRecordService.uploadMedicalRecord(patientId, file, request);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/patient/{patientId}/medical-records")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecords(@PathVariable Long patientId) {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByPatient(patientId);
        return ResponseEntity.ok(records);
    }

    @DeleteMapping("/medical-records/{recordId}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable UUID recordId) {
        medicalRecordService.deleteMedicalRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medical-records/{recordId}/download")
    public ResponseEntity<Resource> downloadMedicalRecord(@PathVariable UUID recordId) {
        MedicalRecord record = medicalRecordService.getMedicalRecordEntity(recordId);
        try {
            Path filePath = Paths.get(record.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + record.getFileName() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}

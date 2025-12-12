package com.medibook.medibook_backend.service;

import com.medibook.medibook_backend.dto.MedicalRecordDTO;
import com.medibook.medibook_backend.dto.UploadMedicalRecordRequest;
import com.medibook.medibook_backend.entity.MedicalRecord;
import com.medibook.medibook_backend.entity.Patient;
import com.medibook.medibook_backend.repository.MedicalRecordRepository;
import com.medibook.medibook_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final FileStorageService fileStorageService;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
            PatientRepository patientRepository,
            FileStorageService fileStorageService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public MedicalRecordDTO uploadMedicalRecord(Long patientId, MultipartFile file,
            UploadMedicalRecordRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        String filePath = fileStorageService.saveMedicalRecord(file);

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setRecordType(request.getRecordType());
        record.setDescription(request.getDescription());
        record.setFileName(file.getOriginalFilename());
        record.setFileType(file.getContentType());
        record.setFileSize(file.getSize());
        record.setFilePath(filePath);
        record.setUploadDate(LocalDateTime.now());

        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        return mapToDTO(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteMedicalRecord(UUID recordId) {
        if (!medicalRecordRepository.existsById(recordId)) {
            throw new RuntimeException("Medical record not found with ID: " + recordId);
        }
        // Note: Actual file deletion from disk could be implemented here as well
        medicalRecordRepository.deleteById(recordId);
    }

    @Transactional(readOnly = true)
    public MedicalRecord getMedicalRecordEntity(UUID recordId) {
        return medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found with ID: " + recordId));
    }

    private MedicalRecordDTO mapToDTO(MedicalRecord record) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(record.getId());
        dto.setPatientId(record.getPatient().getId());
        dto.setRecordType(record.getRecordType());
        dto.setDescription(record.getDescription());
        dto.setFileName(record.getFileName());
        dto.setFileType(record.getFileType());
        dto.setFileSize(record.getFileSize());
        dto.setUploadDate(record.getUploadDate());
        return dto;
    }
}

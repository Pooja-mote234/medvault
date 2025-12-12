package com.medibook.medibook_backend.service;

import com.medibook.medibook_backend.dto.MedicalRecordDTO;
import com.medibook.medibook_backend.dto.UploadMedicalRecordRequest;
import com.medibook.medibook_backend.entity.MedicalRecord;
import com.medibook.medibook_backend.entity.Patient;
import com.medibook.medibook_backend.repository.MedicalRecordRepository;
import com.medibook.medibook_backend.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private Patient patient;
    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        medicalRecord = new MedicalRecord();
        medicalRecord.setId(UUID.randomUUID());
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordType("General Report");
        medicalRecord.setFileName("test.pdf");
        medicalRecord.setFilePath("uploads/medical-records/test.pdf");
    }

    @Test
    void uploadMedicalRecord_Success() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test data".getBytes());
        UploadMedicalRecordRequest request = new UploadMedicalRecordRequest();
        request.setRecordType("X-Ray");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(fileStorageService.saveMedicalRecord(any())).thenReturn("uploads/medical-records/mocked_file.pdf");
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenAnswer(invocation -> {
            MedicalRecord r = invocation.getArgument(0);
            r.setId(UUID.randomUUID());
            return r;
        });

        MedicalRecordDTO result = medicalRecordService.uploadMedicalRecord(1L, file, request);

        assertNotNull(result);
        assertEquals("X-Ray", result.getRecordType());
        verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void getMedicalRecordsByPatient_Success() {
        when(medicalRecordRepository.findByPatientId(1L)).thenReturn(List.of(medicalRecord));

        List<MedicalRecordDTO> results = medicalRecordService.getMedicalRecordsByPatient(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("General Report", results.get(0).getRecordType());
    }

    @Test
    void deleteMedicalRecord_Success() {
        UUID recordId = medicalRecord.getId();
        when(medicalRecordRepository.existsById(recordId)).thenReturn(true);

        medicalRecordService.deleteMedicalRecord(recordId);

        verify(medicalRecordRepository, times(1)).deleteById(recordId);
    }

    @Test
    void deleteMedicalRecord_NotFound() {
        UUID recordId = UUID.randomUUID();
        when(medicalRecordRepository.existsById(recordId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> medicalRecordService.deleteMedicalRecord(recordId));
    }
}

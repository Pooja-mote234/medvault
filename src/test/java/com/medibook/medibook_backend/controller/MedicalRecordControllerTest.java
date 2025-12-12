package com.medibook.medibook_backend.controller;

import com.medibook.medibook_backend.dto.MedicalRecordDTO;
import com.medibook.medibook_backend.dto.UploadMedicalRecordRequest;
import com.medibook.medibook_backend.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    private MedicalRecordDTO medicalRecordDTO;

    @BeforeEach
    void setUp() {
        medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setId(UUID.randomUUID());
        medicalRecordDTO.setRecordType("Test Record");
    }

    @Test
    void uploadMedicalRecord_Success() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "data".getBytes());
        UploadMedicalRecordRequest request = new UploadMedicalRecordRequest();
        request.setRecordType("Test Record");

        when(medicalRecordService.uploadMedicalRecord(eq(1L), any(MultipartFile.class),
                any(UploadMedicalRecordRequest.class)))
                .thenReturn(medicalRecordDTO);

        ResponseEntity<MedicalRecordDTO> response = medicalRecordController.uploadMedicalRecord(1L, file, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Test Record", response.getBody().getRecordType());
    }

    @Test
    void getMedicalRecords_Success() {
        when(medicalRecordService.getMedicalRecordsByPatient(1L)).thenReturn(List.of(medicalRecordDTO));

        ResponseEntity<List<MedicalRecordDTO>> response = medicalRecordController.getMedicalRecords(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deleteMedicalRecord_Success() {
        UUID recordId = UUID.randomUUID();
        doNothing().when(medicalRecordService).deleteMedicalRecord(recordId);

        ResponseEntity<Void> response = medicalRecordController.deleteMedicalRecord(recordId);

        assertEquals(204, response.getStatusCode().value());
    }
}

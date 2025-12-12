package com.medibook.medibook_backend.dto;

public class UploadMedicalRecordRequest {
    private String recordType;
    private String description;

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
